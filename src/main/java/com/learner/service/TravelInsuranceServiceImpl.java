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

import com.learner.dto.TravelInsuranceDTO;
import com.learner.entity.Status;
import com.learner.entity.TravelInsuranceDetails;
import com.learner.exception.InsuranceRelavantException;
import com.learner.exception.UserRelavantException;
import com.learner.repository.TravelInsuranceRepository;


@Service
public class TravelInsuranceServiceImpl{

    private static final Logger logger = LoggerFactory.getLogger(TravelInsuranceServiceImpl.class);

    @Autowired
    private TravelInsuranceRepository travelInsuranceRepository;

    @Autowired
    private EmailService emailService;

    public TravelInsuranceDTO ApplyTravelInsurance(MultipartFile documentimage, String email, String policyId, String destination,String policyName,
                                       String organization, String startTime, String endTime, String modeOfTravel, String ticketId,
                                       String nomineeName, String nomineeRelation, String nomineeAge, String nomineeAadharNo)
            throws IOException, SQLException {

    	if(travelInsuranceRepository.existsByEmail(email))
    	{
    		throw new UserRelavantException("User already exists with email "+email);
    	}
        TravelInsuranceDetails travelInsuranceDetails = new TravelInsuranceDetails();
        travelInsuranceDetails.setEmail(email);
        travelInsuranceDetails.setPolicyNo(generatePolicyNo());
        travelInsuranceDetails.setPolicyId(policyId);
        travelInsuranceDetails.setPolicyName(policyName);
        travelInsuranceDetails.setDestination(destination);
        travelInsuranceDetails.setOrganization(organization);
        travelInsuranceDetails.setStartTime(startTime);
        travelInsuranceDetails.setEndTime(endTime);
        travelInsuranceDetails.setModeOfTravel(modeOfTravel);
        travelInsuranceDetails.setTicketId(ticketId);
        travelInsuranceDetails.setNomineeName(nomineeName);
        travelInsuranceDetails.setNomineeRelation(nomineeRelation);
        travelInsuranceDetails.setNomineeAge(nomineeAge);
        travelInsuranceDetails.setNomineeAadharNo(nomineeAadharNo);
        travelInsuranceDetails.setStatus(Status.PENDING.toString());

        if (!documentimage.isEmpty()) {
            byte[] photoBytes = documentimage.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            travelInsuranceDetails.setDocumentimage(photoBlob);
        }

        TravelInsuranceDetails ti = travelInsuranceRepository.save(travelInsuranceDetails);
        TravelInsuranceDTO travelInsuranceDTO = new TravelInsuranceDTO();
        travelInsuranceDTO.setId(ti.getId());
        travelInsuranceDTO.setEmail(ti.getEmail());
        travelInsuranceDTO.setPolicyNo(ti.getPolicyNo());
        travelInsuranceDTO.setPolicyId(ti.getPolicyId());
        travelInsuranceDTO.setPolicyName(ti.getPolicyName());
        travelInsuranceDTO.setDestination(ti.getDestination());
        travelInsuranceDTO.setOrganization(ti.getOrganization());
        travelInsuranceDTO.setStartTime(ti.getStartTime());
        travelInsuranceDTO.setEndTime(ti.getEndTime());
        travelInsuranceDTO.setModeOfTravel(ti.getModeOfTravel());
        travelInsuranceDTO.setTicketId(ti.getTicketId());
        travelInsuranceDTO.setNomineeName(ti.getNomineeName());
        travelInsuranceDTO.setNomineeRelation(ti.getNomineeRelation());
        travelInsuranceDTO.setNomineeAge(ti.getNomineeAge());
        travelInsuranceDTO.setNomineeAadharNo(ti.getNomineeAadharNo());
        
        

        String subject = "Policy Applied Successfully";
        String text = "Your policy has been applied successfully. Your policy number: " + travelInsuranceDetails.getPolicyNo();

        try {
            emailService.sendMail(travelInsuranceDetails.getEmail(), subject, text);
        } catch (MailSendException e) {
            logger.error("Failed to send policy confirmation email due to mail server connection issue", e);
        } catch (Exception e) {
            logger.error("Failed to send policy confirmation email", e);
        }

        return travelInsuranceDTO;
    }

    private String generatePolicyNo() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public List<TravelInsuranceDTO> AllApplications() {
        List<TravelInsuranceDetails> applications = travelInsuranceRepository.findAll();
        return applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TravelInsuranceDTO convertToDTO(TravelInsuranceDetails travelInsuranceDetails) {
        TravelInsuranceDTO travelInsuranceDTO = new TravelInsuranceDTO();
        travelInsuranceDTO.setId(travelInsuranceDetails.getId());
        travelInsuranceDTO.setEmail(travelInsuranceDetails.getEmail());
        travelInsuranceDTO.setType(travelInsuranceDetails.getType());
        travelInsuranceDTO.setPolicyNo(travelInsuranceDetails.getPolicyNo());
        travelInsuranceDTO.setPolicyId(travelInsuranceDetails.getPolicyId());
        travelInsuranceDTO.setPolicyName(travelInsuranceDetails.getPolicyName());
        travelInsuranceDTO.setDestination(travelInsuranceDetails.getDestination());
        travelInsuranceDTO.setOrganization(travelInsuranceDetails.getOrganization());
        travelInsuranceDTO.setStartTime(travelInsuranceDetails.getStartTime());
        travelInsuranceDTO.setEndTime(travelInsuranceDetails.getEndTime());
        travelInsuranceDTO.setModeOfTravel(travelInsuranceDetails.getModeOfTravel());
        travelInsuranceDTO.setTicketId(travelInsuranceDetails.getTicketId());
        travelInsuranceDTO.setNomineeName(travelInsuranceDetails.getNomineeName());
        travelInsuranceDTO.setNomineeRelation(travelInsuranceDetails.getNomineeRelation());
        travelInsuranceDTO.setNomineeAge(travelInsuranceDetails.getNomineeAge());
        travelInsuranceDTO.setNomineeAadharNo(travelInsuranceDetails.getNomineeAadharNo());
        travelInsuranceDTO.setStatus(travelInsuranceDetails.getStatus());

        if (travelInsuranceDetails.getDocumentimage() != null) {
            try {
                byte[] photoBytes = travelInsuranceDetails.getDocumentimage().getBytes(1, (int) travelInsuranceDetails.getDocumentimage().length());
                String base64photo = Base64.getEncoder().encodeToString(photoBytes);
                travelInsuranceDTO.setDocumentimage(base64photo);
            } catch (SQLException e) {
                logger.error("Error converting document image to base64", e);
            }
        }

        return travelInsuranceDTO;
    }


    public String statusApproval(String policyNo, String status) {
        TravelInsuranceDetails travelInsuranceDetails=travelInsuranceRepository.findByPolicyNo(policyNo).orElseThrow(
        		() -> new InsuranceRelavantException("Vehicle insurance application not found with id "+policyNo)
        		);
            if(status.equalsIgnoreCase(Status.APPROVED.toString()))
 	       {
            	travelInsuranceDetails.setStatus(Status.APPROVED.toString());
 	       }
 	       else if(status.equalsIgnoreCase(Status.REJECTED.toString()))
 	       {
 	    	  travelInsuranceDetails.setStatus(Status.REJECTED.toString());
 	       }
 	       else {
 	    	   throw new UserRelavantException("Internal server error");
 	       }
            travelInsuranceRepository.save(travelInsuranceDetails);
             String subject = "Policy Approved";
             String text = "Your policy having policy number" + travelInsuranceDetails.getPolicyNo() + " has been "+travelInsuranceDetails.getStatus()+" .";
             try {
                 emailService.sendMail(travelInsuranceDetails.getEmail(), subject, text);
             } catch (MailSendException e) {
                 logger.error("Failed to send approval confirmation email due to mail server connection issue", e);
             } catch (Exception e) {
                 logger.error("Failed to send booking confirmation email", e);
             }
             return "Status Updated";
        
        }
    
    public TravelInsuranceDTO getUserPolicy(String email) {
        Optional<TravelInsuranceDetails> opt=travelInsuranceRepository.findByEmail(email);
       if(opt.isPresent())
       {
    	   TravelInsuranceDetails ti = opt.get();
    	   TravelInsuranceDTO iDTO =new TravelInsuranceDTO();
           iDTO.setId(ti.getId());
           iDTO.setEmail(ti.getEmail());
           iDTO.setPolicyId(ti.getPolicyId());
           iDTO.setPolicyNo(ti.getPolicyNo());
           iDTO.setDestination(ti.getDestination());
           iDTO.setOrganization(ti.getOrganization());
           iDTO.setStartTime(ti.getStartTime());
           iDTO.setEndTime(ti.getEndTime());
           iDTO.setModeOfTravel(ti.getModeOfTravel());
           iDTO.setNomineeName(ti.getNomineeName());
           iDTO.setNomineeAge(ti.getNomineeAge());
           iDTO.setNomineeRelation(ti.getNomineeRelation());
           iDTO.setNomineeAadharNo(ti.getNomineeAadharNo());
           iDTO.setTicketId(ti.getTicketId());
           iDTO.setPolicyName(ti.getPolicyName());
           iDTO.setStatus(ti.getStatus());
           return iDTO;
       }
            
       else
       {
    	   return null;
       }
       
       
    }

	public TravelInsuranceDTO getPolicyByPolicyno(String policyNo) {
		TravelInsuranceDetails req = travelInsuranceRepository.findByPolicyNo(policyNo).orElseThrow();
		return convertToDTO(req);
	}
}
