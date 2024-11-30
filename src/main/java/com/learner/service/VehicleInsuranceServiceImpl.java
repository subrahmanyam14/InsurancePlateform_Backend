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

import com.learner.dto.VehicleInsuranceDto;
import com.learner.entity.Status;
import com.learner.entity.VehicleInsuranceDetails;
import com.learner.exception.InsuranceRelavantException;
import com.learner.exception.UserRelavantException;
import com.learner.repository.VehicleInsuranceRepository;



@Service
public class VehicleInsuranceServiceImpl{

    private static final Logger logger = LoggerFactory.getLogger(VehicleInsuranceServiceImpl.class);

    @Autowired
    private VehicleInsuranceRepository vehicleInsuranceRepository;

    @Autowired
    private EmailService emailService;  
    public VehicleInsuranceDto ApplyVehicleInsurance(MultipartFile documentimage, String email, String policyId,String policyName,
            String vehicleNumber, String vehicleCompany, String vehicleModel, String chassisNumber, 
            String manufacturingYear) throws IOException, SQLException {
    	if(vehicleInsuranceRepository.existsByEmail(email))
    	{
    		throw new UserRelavantException("User already exists with email "+email);
    	}
        VehicleInsuranceDetails vehicleInsuranceDetails = new VehicleInsuranceDetails();
        vehicleInsuranceDetails.setEmail(email);
        vehicleInsuranceDetails.setPolicyNo(generatePolicyNo());
        vehicleInsuranceDetails.setVehicleNumber(vehicleNumber);
        vehicleInsuranceDetails.setVehicleCompany(vehicleCompany);
        vehicleInsuranceDetails.setVehicleModel(vehicleModel);
        vehicleInsuranceDetails.setPolicyId(policyId );
        vehicleInsuranceDetails.setPolicyName(policyName);
        vehicleInsuranceDetails.setChassisNumber(chassisNumber);
        vehicleInsuranceDetails.setManufacturingYear(manufacturingYear);
        vehicleInsuranceDetails.setStatus(Status.PENDING.toString());
        

        if (!documentimage.isEmpty()) {
            byte[] photoBytes = documentimage.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            vehicleInsuranceDetails.setDocumentimage(photoBlob);
        }

        VehicleInsuranceDetails vehicle = vehicleInsuranceRepository.save(vehicleInsuranceDetails);

        String subject = "Policy Applied Successfully";
        String text = "Your policy has been applied successfully. Your respective policy number: " + vehicleInsuranceDetails.getPolicyNo();
        
        try {
            emailService.sendMail(vehicleInsuranceDetails.getEmail(), subject, text);
        } catch (MailSendException e) {
            logger.error("Failed to send policy confirmation email due to mail server connection issue", e);
        } catch (Exception e) {
            logger.error("Failed to send policy confirmation email", e);
        }

        return convertToDTO(vehicle);
    }

    private String generatePolicyNo() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public List<VehicleInsuranceDto> AllApplications() {
        List<VehicleInsuranceDetails> applications=vehicleInsuranceRepository.findAll();
        return applications.stream().map(this::convertToDTO).collect(Collectors.toList());
      
    }
    private VehicleInsuranceDto convertToDTO(VehicleInsuranceDetails VehicleInsuranceDetails) {
        VehicleInsuranceDto vehicleInsuranceDTO = new VehicleInsuranceDto();
        vehicleInsuranceDTO.setId(VehicleInsuranceDetails.getId());
        vehicleInsuranceDTO.setEmail(VehicleInsuranceDetails.getEmail());
        vehicleInsuranceDTO.setType(VehicleInsuranceDetails.getType());
        vehicleInsuranceDTO.setPolicyNo(VehicleInsuranceDetails.getPolicyNo());
        vehicleInsuranceDTO.setVehicleNumber(VehicleInsuranceDetails.getVehicleNumber());
        vehicleInsuranceDTO.setVehicleModel(VehicleInsuranceDetails.getVehicleModel());
        vehicleInsuranceDTO.setVehicleCompany(VehicleInsuranceDetails.getVehicleCompany());
        vehicleInsuranceDTO.setPolicyId(VehicleInsuranceDetails.getPolicyId());
        vehicleInsuranceDTO.setPolicyName(VehicleInsuranceDetails.getPolicyName());
        vehicleInsuranceDTO.setChassisNumber(VehicleInsuranceDetails.getChassisNumber());
        vehicleInsuranceDTO.setManufacturingYear(VehicleInsuranceDetails.getManufacturingYear());
        vehicleInsuranceDTO.setStatus(VehicleInsuranceDetails.getStatus());
        if (VehicleInsuranceDetails.getDocumentimage() != null) {
            try {
                var photoBytes = VehicleInsuranceDetails.getDocumentimage().getBytes(1, (int) VehicleInsuranceDetails.getDocumentimage().length());
                String base64photo = Base64.getEncoder().encodeToString(photoBytes);
                vehicleInsuranceDTO.setDocumentimage(base64photo);
            } catch (SQLException e) {
               
            }
        }

        return vehicleInsuranceDTO;
    }

  
    public String statusApproval(String id, String status) {
        VehicleInsuranceDetails vehicleInsuranceDetails=vehicleInsuranceRepository.findByPolicyNo(id).orElseThrow(
        		() -> new InsuranceRelavantException("Vehicle insurance application not found with id "+id)
        		);
            if(status.equalsIgnoreCase(Status.APPROVED.toString()))
 	       {
            	vehicleInsuranceDetails.setStatus(Status.APPROVED.toString());
 	       }
 	       else if(status.equalsIgnoreCase(Status.REJECTED.toString()))
 	       {
 	    	  vehicleInsuranceDetails.setStatus(Status.REJECTED.toString());
 	       }
 	       else {
 	    	   throw new UserRelavantException("Internal server error");
 	       }
            vehicleInsuranceRepository.save(vehicleInsuranceDetails);
             String subject = "Policy Approved";
             String text = "Your policy having policy number" + vehicleInsuranceDetails.getPolicyNo() + " has been "+vehicleInsuranceDetails.getStatus()+" .";
             try {
                 emailService.sendMail(vehicleInsuranceDetails.getEmail(), subject, text);
             } catch (MailSendException e) {
                 logger.error("Failed to send approval confirmation email due to mail server connection issue", e);
             } catch (Exception e) {
                 logger.error("Failed to send booking confirmation email", e);
             }
             return "Status Updated";
        
        }
    
    
    public VehicleInsuranceDto mypolicy(String email) {
        Optional<VehicleInsuranceDetails> vehicleInsurancedetails=vehicleInsuranceRepository.findByEmail(email);
        if(vehicleInsurancedetails.isPresent())
        {
        	VehicleInsuranceDetails vehicleInsurancedetail = vehicleInsurancedetails.get();
        	VehicleInsuranceDto vehicleInsuranceDTO=new VehicleInsuranceDto();
            vehicleInsuranceDTO.setEmail(vehicleInsurancedetail.getEmail());
            vehicleInsuranceDTO.setVehicleCompany(vehicleInsurancedetail.getVehicleCompany());
            vehicleInsuranceDTO.setVehicleModel(vehicleInsurancedetail.getVehicleModel());
            vehicleInsuranceDTO.setVehicleNumber(vehicleInsurancedetail.getVehicleNumber());
            vehicleInsuranceDTO.setChassisNumber(vehicleInsurancedetail.getChassisNumber());
            vehicleInsuranceDTO.setManufacturingYear(vehicleInsurancedetail.getManufacturingYear());
            vehicleInsuranceDTO.setPolicyNo(vehicleInsurancedetail.getPolicyNo());
            vehicleInsuranceDTO.setPolicyId(vehicleInsurancedetail.getPolicyId());
            vehicleInsuranceDTO.setPolicyName(vehicleInsurancedetail.getPolicyName());
            vehicleInsuranceDTO.setStatus(vehicleInsurancedetail.getStatus());
            vehicleInsuranceDTO.setId(vehicleInsurancedetail.getId());
            return vehicleInsuranceDTO;
        }
          
        else {
        	return null;
        }
          
      
        
    }

	public VehicleInsuranceDto getPolicyByPolicyno(String policyNo) {
		VehicleInsuranceDetails req = vehicleInsuranceRepository.findByPolicyNo(policyNo).orElseThrow();
		return convertToDTO(req);
	}
    
}
