package com.example.regionazservice.controller;

import com.example.regionazservice.payload.AWSRegionAZResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.regions.internal.util.EC2MetadataUtils;

@RestController
@RequestMapping("/api")
public class AWSRegionController {

	@GetMapping("/aws/location")
	public AWSRegionAZResponse getAwsLocation(){
		String region = EC2MetadataUtils.getEC2InstanceRegion();
		String availabilityZone = EC2MetadataUtils.getAvailabilityZone();
		return new AWSRegionAZResponse(region, availabilityZone);
	}


}
