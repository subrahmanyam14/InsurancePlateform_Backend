package com.learner.service;


import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.learner.dto.HomeInsuranceDTO;
import com.learner.entity.HomeInsuranceDetails;
import com.learner.entity.Status;
import com.learner.exception.InsuranceRelavantException;
import com.learner.exception.UserRelavantException;
import com.learner.repository.HomeInsuranceRepository;



@Service
public class HomeInsuranceServiceImpl{

    private static final Logger logger = LoggerFactory.getLogger(HomeInsuranceServiceImpl.class);

    @Autowired
    private HomeInsuranceRepository homeInsuranceRepository;

    @Autowired
    private EmailService emailService;  
   
    public HomeInsuranceDTO ApplyHomeInsurance(MultipartFile documentimage, String email, String policyId, String houseno,String policyName,
    String owner, String location) throws IOException, SQLException {
    	
    	if(homeInsuranceRepository.existsByEmail(email))
    	{
    		throw new UserRelavantException("User already exists with email "+email);
    	}
        
        HomeInsuranceDetails homeInsuranceDetails = new HomeInsuranceDetails();
        homeInsuranceDetails.setEmail(email);
        homeInsuranceDetails.setPolicyNo(generatePolicyNo());
        homeInsuranceDetails.setPolicyId(policyId);
        homeInsuranceDetails.setPolicyName(policyName);
        homeInsuranceDetails.setLocation(location);
        homeInsuranceDetails.setOwner(owner);
        homeInsuranceDetails.setHouseno(houseno);
        homeInsuranceDetails.setStatus(Status.PENDING.toString());
                if (!documentimage.isEmpty()) {
            byte[] photoBytes = documentimage.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            homeInsuranceDetails.setDocumentimage(photoBlob);
        }
        HomeInsuranceDetails home = homeInsuranceRepository.save(homeInsuranceDetails);

        String subject = "Policy Applied Successfully";
        String text = "Your policy has been applied successfully. Your respective policy number: " + homeInsuranceDetails.getPolicyNo();
        
        try {
            emailService.sendMail(homeInsuranceDetails.getEmail(), subject, text);
        } catch (MailSendException e) {
            logger.error("Failed to send policy confirmation email due to mail server connection issue", e);
        } catch (Exception e) {
            logger.error("Failed to send policy confirmation email", e);
        }

        return convertToDTO(home);
    }

    private String generatePolicyNo() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

   
    public List<HomeInsuranceDTO> AllApplications() {
        List<HomeInsuranceDetails> applications=homeInsuranceRepository.findAll();
        return applications.stream().map(this::convertToDTO).collect(Collectors.toList());
      
    }
    private HomeInsuranceDTO convertToDTO(HomeInsuranceDetails homeInsuranceDetails) {
        HomeInsuranceDTO homeInsuranceDTO = new HomeInsuranceDTO();
        homeInsuranceDTO.setId(homeInsuranceDetails.getId());
        homeInsuranceDTO.setEmail(homeInsuranceDetails.getEmail());
        homeInsuranceDTO.setType(homeInsuranceDetails.getType());
        homeInsuranceDTO.setPolicyId(homeInsuranceDetails.getPolicyId());
        homeInsuranceDTO.setPolicyNo(homeInsuranceDetails.getPolicyNo());
        homeInsuranceDTO.setPolicyName(homeInsuranceDetails.getPolicyName());
        homeInsuranceDTO.setLocation(homeInsuranceDetails.getLocation());
        homeInsuranceDTO.setHouseno(homeInsuranceDetails.getHouseno());
        homeInsuranceDTO.setOwner(homeInsuranceDetails.getOwner());
     
        homeInsuranceDTO.setStatus(homeInsuranceDetails.getStatus());
        if (homeInsuranceDetails.getDocumentimage() != null) {
            try {
                var photoBytes = homeInsuranceDetails.getDocumentimage().getBytes(1, (int) homeInsuranceDetails.getDocumentimage().length());
                String base64photo = Base64.getEncoder().encodeToString(photoBytes);
               homeInsuranceDTO.setDocumentimage(base64photo);
            } catch (SQLException e) {
               
            }
        }

        return homeInsuranceDTO;
    }

    
    public String statusApproval(String policyNo, String status) {
        HomeInsuranceDetails homeInsuranceDetails=homeInsuranceRepository.findByPolicyNo(policyNo).orElseThrow(
        		() -> new InsuranceRelavantException("Home insurance application not found with id "+policyNo)
        		);
            if(status.equalsIgnoreCase(Status.APPROVED.toString()))
 	       {
            	homeInsuranceDetails.setStatus(Status.APPROVED.toString());
 	       }
 	       else if(status.equalsIgnoreCase(Status.REJECTED.toString()))
 	       {
 	    	  homeInsuranceDetails.setStatus(Status.REJECTED.toString());
 	       }
 	       else {
 	    	   throw new UserRelavantException("Internal server error");
 	       }
            homeInsuranceRepository.save(homeInsuranceDetails);
             String subject = "Policy Approved";
             String text = "Your policy having policy number" + homeInsuranceDetails.getPolicyNo() + " has been "+homeInsuranceDetails.getStatus()+" .";
             try {
                 emailService.sendMail(homeInsuranceDetails.getEmail(), subject, text);
             } catch (MailSendException e) {
                 logger.error("Failed to send approval confirmation email due to mail server connection issue", e);
             } catch (Exception e) {
                 logger.error("Failed to send booking confirmation email", e);
             }
             return "Status Updated";
        
        }

    public HomeInsuranceDTO mypolicy(String email) {
        Optional<HomeInsuranceDetails> homeInsurancedetails=homeInsuranceRepository.findByEmail(email);
       if(homeInsurancedetails.isPresent())
       {
          HomeInsuranceDetails homeInsuranceDetails=homeInsurancedetails.get();
          HomeInsuranceDTO homeInsuranceDTO=new HomeInsuranceDTO();
          homeInsuranceDTO.setEmail(homeInsuranceDetails.getEmail());
          homeInsuranceDTO.setType(homeInsuranceDetails.getType());
          homeInsuranceDTO.setHouseno(homeInsuranceDetails.getHouseno());
          homeInsuranceDTO.setLocation(homeInsuranceDetails.getLocation());
          homeInsuranceDTO.setOwner(homeInsuranceDetails.getOwner());
          homeInsuranceDTO.setPolicyNo(homeInsuranceDetails.getPolicyNo());
          homeInsuranceDTO.setPolicyId(homeInsuranceDetails.getPolicyId());
          homeInsuranceDTO.setPolicyName(homeInsuranceDetails.getPolicyName());
          homeInsuranceDTO.setStatus(homeInsuranceDetails.getStatus());
          homeInsuranceDTO.setId(homeInsuranceDetails.getId());

          return homeInsuranceDTO;
       }
       return null;
       



}

	public HomeInsuranceDTO getPolicyByPolicyno(String policyNo) {
		HomeInsuranceDetails req = homeInsuranceRepository.findByPolicyNo(policyNo).orElseThrow();
				
		return convertToDTO(req);
	}

    
    
}