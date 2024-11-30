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

import com.learner.dto.HealthInsuranceDTO;
import com.learner.entity.HealthInsuranceDetails;
import com.learner.entity.Status;
import com.learner.exception.InsuranceRelavantException;
import com.learner.exception.ResourceNotFoundException;
import com.learner.exception.UserRelavantException;
import com.learner.repository.HealthInsuranceRepository;


@Service
public class HealthInsuranceServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(HealthInsuranceServiceImpl.class);

    @Autowired
    private HealthInsuranceRepository healthInsuranceRepository;

    @Autowired
    private EmailService emailService; 
    
    private HealthInsuranceDTO toDto(HealthInsuranceDetails req)
    {
    	HealthInsuranceDTO dto = new HealthInsuranceDTO();
    	dto.setCurrent_medication(req.getCurrent_medication());
    	dto.setEmail(req.getEmail());
    	dto.setExisting_medical_condition(req.getExisting_medical_condition());
    	dto.setId(req.getId());
    	dto.setPolicyId(req.getPolicyId());
    	dto.setPolicyNo(req.getPolicyNo());
    	dto.setPolicyName(req.getPolicyName());
    	dto.setStatus(req.getStatus());
    	return dto;
    }
    
   
     public HealthInsuranceDTO ApplyHealthInsurance(MultipartFile documentimage,String email,String policyId,String policyName, String existing_medical_condition,String current_medication) throws IOException, SQLException
 {
        if(healthInsuranceRepository.existsByEmail(email))
        {
        	throw new InsuranceRelavantException("User already exists with mail "+email); 
        }
        HealthInsuranceDetails healthInsuranceDetails = new HealthInsuranceDetails();
        healthInsuranceDetails.setEmail(email);
        healthInsuranceDetails.setPolicyNo(generatePolicyNo());
        healthInsuranceDetails.setPolicyId(policyId);
        healthInsuranceDetails.setPolicyName(policyName);
        healthInsuranceDetails.setExisting_medical_condition(existing_medical_condition);
        healthInsuranceDetails.setCurrent_medication(current_medication);
        healthInsuranceDetails.setStatus(Status.PENDING.toString());
                if (!documentimage.isEmpty()) {
            byte[] photoBytes = documentimage.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            healthInsuranceDetails.setDocumentimage(photoBlob);
        }

        HealthInsuranceDetails saved = healthInsuranceRepository.save(healthInsuranceDetails);

        String subject = "Policy Applied Successfully";
        String text = "Your policy has been applied successfully. Your respective policy number: " + healthInsuranceDetails.getPolicyNo();
        
        try {
            emailService.sendMail(healthInsuranceDetails.getEmail(), subject, text);
        } catch (MailSendException e) {
            logger.error("Failed to send policy confirmation email due to mail server connection issue", e);
        } catch (Exception e) {
            logger.error("Failed to send policy confirmation email", e);
        }

        return toDto(saved);
    }

    private String generatePolicyNo() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

  
    public List<HealthInsuranceDTO> AllApplications() {
        List<HealthInsuranceDetails> applications=healthInsuranceRepository.findAll();
        return applications.stream().map(this::convertToDTO).collect(Collectors.toList());
      
    }
    private HealthInsuranceDTO convertToDTO(HealthInsuranceDetails healthInsuranceDetails) {
        HealthInsuranceDTO healthInsuranceDTO = new HealthInsuranceDTO();
        healthInsuranceDTO.setId(healthInsuranceDetails.getId());
        healthInsuranceDTO.setEmail(healthInsuranceDetails.getEmail());
        healthInsuranceDTO.setType(healthInsuranceDetails.getType());
        healthInsuranceDTO.setPolicyId(healthInsuranceDetails.getPolicyId());
        healthInsuranceDTO.setPolicyNo(healthInsuranceDetails.getPolicyNo());
        healthInsuranceDTO.setPolicyName(healthInsuranceDetails.getPolicyName());
        healthInsuranceDTO.setExisting_medical_condition(healthInsuranceDetails.getExisting_medical_condition());
        healthInsuranceDTO.setCurrent_medication(healthInsuranceDetails.getExisting_medical_condition());
     
        healthInsuranceDTO.setStatus(healthInsuranceDetails.getStatus());
        if (healthInsuranceDetails.getDocumentimage() != null) {
            try {
                var photoBytes = healthInsuranceDetails.getDocumentimage().getBytes(1, (int) healthInsuranceDetails.getDocumentimage().length());
                String base64photo = Base64.getEncoder().encodeToString(photoBytes);
               healthInsuranceDTO.setDocumentimage(base64photo);
            } catch (SQLException e) {
               
            }
        }

        return healthInsuranceDTO;
    }

   
    public String statusApproval(String policyNo, String status) {
        HealthInsuranceDetails healthInsuranceDetails=healthInsuranceRepository.findByPolicyNo(policyNo).orElseThrow(
        		() -> new InsuranceRelavantException("Health insurance application not found with id "+policyNo)
        		);
            if(status.equalsIgnoreCase(Status.APPROVED.toString()))
 	       {
            	healthInsuranceDetails.setStatus(Status.APPROVED.toString());
 	       }
 	       else if(status.equalsIgnoreCase(Status.REJECTED.toString()))
 	       {
 	    	  healthInsuranceDetails.setStatus(Status.REJECTED.toString());
 	       }
 	       else {
 	    	   throw new UserRelavantException("Internal server error");
 	       }
            healthInsuranceRepository.save(healthInsuranceDetails);
             String subject = "Policy Approved";
             String text = "Your policy having policy number" + healthInsuranceDetails.getPolicyNo() + " has been "+healthInsuranceDetails.getStatus()+" .";
             try {
                 emailService.sendMail(healthInsuranceDetails.getEmail(), subject, text);
             } catch (MailSendException e) {
                 logger.error("Failed to send approval confirmation email due to mail server connection issue", e);
             } catch (Exception e) {
                 logger.error("Failed to send booking confirmation email", e);
             }
             return "Status Updated";
        
        }
    public HealthInsuranceDTO mypolicy(String email) {
          Optional<HealthInsuranceDetails> healthInsuranceDetails=healthInsuranceRepository.findByEmail(email);
          if(healthInsuranceDetails.isPresent())
          {
        	  HealthInsuranceDetails healthInsuranceDetail =  healthInsuranceDetails.get(); 
        	  HealthInsuranceDTO healthInsuranceDTO=new HealthInsuranceDTO();
              healthInsuranceDTO.setEmail(healthInsuranceDetail.getEmail());
              healthInsuranceDTO.setType(healthInsuranceDetail.getType());
              healthInsuranceDTO.setExisting_medical_condition(healthInsuranceDetail.getExisting_medical_condition());
              healthInsuranceDTO.setCurrent_medication(healthInsuranceDetail.getCurrent_medication());
              healthInsuranceDTO.setPolicyNo(healthInsuranceDetail.getPolicyNo());
              healthInsuranceDTO.setPolicyId(healthInsuranceDetail.getPolicyId());
              healthInsuranceDTO.setPolicyName(healthInsuranceDetail.getPolicyName());
              healthInsuranceDTO.setStatus(healthInsuranceDetail.getStatus());
              healthInsuranceDTO.setId(healthInsuranceDetail.getId());

              return healthInsuranceDTO;
        	  
          }
          else
          {
        	  return null;
          }
          
          
     
    
    
}
    public HealthInsuranceDTO getPolicyByPolicyno(String policyNo)
    {
    	HealthInsuranceDetails req = healthInsuranceRepository.findByPolicyNo(policyNo).orElseThrow(
    			() -> new ResourceNotFoundException("health deatils not found with policyno "+policyNo)
    			);
    	return toDto(req);
    	
    }
}