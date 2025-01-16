package com.awscourseproject.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Properties;

public class DataConsistencyFunction  implements RequestHandler<S3Event, Void> {

    private static final EntityManagerFactory entityManagerFactory ;
    static {
        Properties properties = new Properties();
        properties.put("hibernate.connection.url", System.getenv("DB_URL"));
        properties.put("hibernate.connection.username", System.getenv("DB_USERNAME"));
        properties.put("hibernate.connection.password", System.getenv("DB_PASSWORD"));
        entityManagerFactory = Persistence.createEntityManagerFactory("Images", properties);
    }



    @Override
    public Void handleRequest(S3Event event, Context context) {
        for (S3EventNotification.S3EventNotificationRecord records : event.getRecords()) {
            String bucketName = records.getS3().getBucket().getName();
            String objectKey = records.getS3().getObject().getKey();
            context.getLogger().log("Received S3 event for object: " + objectKey + " in bucket: " + bucketName);

            // Check if the image exists in the RDS database
            boolean exists = checkImageExists(objectKey, context);
            if (exists) {
                context.getLogger().log("Image with name " + objectKey + " exists in the database.");
            } else {
                context.getLogger().log("Image with name " + objectKey + " does not exist in the database.");
            }
        }
        return null;
    }

    private boolean checkImageExists(String imageName, Context context) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Long count = (Long) em.createQuery( "SELECT COUNT(i) FROM ImageMetadata i WHERE CONCAT(i.imageName, i.fileExtension) = :name")
                    .setParameter("name", imageName)
                    .getSingleResult();
            em.getTransaction().commit();
            return count > 0;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            context.getLogger().log("Error checking image existence: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
}