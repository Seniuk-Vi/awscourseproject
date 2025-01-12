RDS IAM auth 

## Useful commands


EC2:
application location /usr
httpd location - /etc/httpd
startup script location - /ect/systemd/system/..

	run application
		sudo systemctl start myapp
		sudo systemctl enable myapp
		sudo systemctl status myapp
		sudo systemctl restart myapp
		sudo systemctl restart mys3rdsapp

	aws s3 cp s3://artifactspringbootbucket/regionazservice-0.0.1-SNAPSHOT.jar /usr/regionazservice-0.0.1-SNAPSHOT.jar
	aws s3 cp s3://artifactspringbootbucket/ImageS3RDSService-0.0.1-SNAPSHOT.jar /usr/ImageS3RDSService-0.0.1-SNAPSHOT.jar

http://34.207.119.31/swagger-ui/index.html#/