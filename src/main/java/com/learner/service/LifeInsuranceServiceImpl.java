package com.learner.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;

import com.learner.dto.LifeInsuranceDTO;
import com.learner.entity.LifeInsurance;
import com.learner.entity.Status;
import com.learner.exception.UserRelavantException;
import com.learner.repository.LifeInsuranceRepository;


@Service
public class LifeInsuranceServiceImpl{
    @Autowired
    private LifeInsuranceRepository lifeInsuranceRepository;
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(LifeInsuranceServiceImpl.class);
    @Autowired
    private EmailService emailService;
    
    private LifeInsuranceDTO todTO(LifeInsurance req)
    {
    	
    	LifeInsuranceDTO dto = new LifeInsuranceDTO();
    	dto.setEmail(req.getEmail());
    	dto.setType(req.getType());
    	dto.setId(req.getId());
    	dto.setNomineeAadharnumber(req.getNomineeAadharnumber());
    	dto.setNomineeAge(req.getNomineeAge());
    	dto.setNomineeName(req.getNomineeName());
    	dto.setNomineeRelation(req.getNomineeRelation());
    	dto.setPolicyId(req.getPolicyId());
    	dto.setPolicyName(req.getPolicyName());
    	dto.setPolicyNo(req.getPolicyNo());
    	dto.setStatus(req.getStatus());
    	return dto;
    }
    public LifeInsuranceDTO ApplyLifeInsurance(String email,String policyId, String policyName,String nomineeName,String nomineeAge,String nomineeRelation,String nomineeAadharnumber) {
       if(lifeInsuranceRepository.existsByEmail(email))
       {
    	   throw new UserRelavantException("User already exists by email "+email);
       }
       LifeInsurance lifeInsurance=new LifeInsurance();
       lifeInsurance.setEmail(email);
       lifeInsurance.setPolicyNo(generatePolicyNo());
       lifeInsurance.setPolicyId(policyId);
       lifeInsurance.setPolicyName(policyName);
       lifeInsurance.setNomineeName(nomineeName);
       lifeInsurance.setNomineeAadharnumber(nomineeAadharnumber);
       lifeInsurance.setNomineeAge(nomineeAge);
       lifeInsurance.setStatus(Status.PENDING.toString());
       lifeInsurance.setNomineeRelation(nomineeRelation);
       lifeInsuranceRepository.save(lifeInsurance);
       

        String subject = "Policy Applied Successfully";
        String text = "Your policy has been applied successfully. Your respective policy number: " + lifeInsurance.getPolicyNo();
        
        try {
            emailService.sendMail(lifeInsurance.getEmail(), subject, text);
        } catch (MailSendException e) {
         
        } catch (Exception e) {
           
        }

        return todTO(lifeInsurance) ;
    }

    private String generatePolicyNo() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }


    public List<LifeInsurance> ViewAll() {
       return lifeInsuranceRepository.findAll();
    }
  
    public LifeInsuranceDTO getmypolicy(String email) {
       Optional<LifeInsurance> lifeInsurance=lifeInsuranceRepository.findByEmail(email);
       if(lifeInsurance.isPresent())
       {
    	   return todTO(lifeInsurance.get());
       }
       else 
       {
    	   return null;
       }
    }

	public void updateStatus(String policyNo, String status) {
		
	       LifeInsurance lifeInsurance = lifeInsuranceRepository.findByPolicyNo(policyNo).orElseThrow(
	    		   () -> new UserRelavantException("Life insurance not found with id "+policyNo)
	    		   );
	       if(status.equalsIgnoreCase(Status.APPROVED.toString()))
	       {
	    	   lifeInsurance.setStatus(Status.APPROVED.toString());
	       }
	       else if(status.equalsIgnoreCase(Status.REJECTED.toString()))
	       {
	    	   lifeInsurance.setStatus(Status.APPROVED.toString());
	       }
	       else {
	    	   throw new UserRelavantException("Internal server error");
	       }
	       
	        String subject = "Policy Approved";
            String text = "Your policy having policy number" + lifeInsurance.getPolicyNo() + " has been "+lifeInsurance.getStatus()+" .";
            try {
                emailService.sendMail(lifeInsurance.getEmail(), subject, text);
            } catch (MailSendException e) {
                logger.error("Failed to send approval confirmation email due to mail server connection issue", e);
            } catch (Exception e) {
                logger.error("Failed to send booking confirmation email", e);
            }
	       lifeInsuranceRepository.save(lifeInsurance);
		
	}
	public  LifeInsuranceDTO getPolicyByPolicyno(String policyNo) {
		
		LifeInsurance req = lifeInsuranceRepository.findByPolicyNo(policyNo).orElseThrow();
		return todTO(req);
	}
	}