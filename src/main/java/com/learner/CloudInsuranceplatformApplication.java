package com.learner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.learner.service.UserInfoService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class CloudInsuranceplatformApplication {
	@Autowired
	private UserInfoService infoService;

	public static void main(String[] args) {
		SpringApplication.run(CloudInsuranceplatformApplication.class, args);
		
	}
	
	@PostConstruct
	private void addAdmin()
	{
		infoService.saveAdminByDefault("admin@gmail.com", "567890123412", "admin@123");
		infoService.saveAgentByDefault("agent@gmail.com", "567890123413", "agent@123");
	}

}
