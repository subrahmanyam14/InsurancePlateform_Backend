package com.learner.service;


import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.learner.dto.ClaimDto;
import com.learner.entity.Claim;
import com.learner.entity.Status;
import com.learner.repository.ClaimRepository;





@Service
public class ClaimServiceImpl {

    @Autowired
    private ClaimRepository claimRepository;
    

    @Autowired
    private EmailService emailService;
    
  

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(VehicleInsuranceServiceImpl.class);
    
    public String saveClaims(String email, String policyName,String policyId,String policyNo,String description, MultipartFile file) throws SerialException, SQLException {
        try {
        	Claim claim = new Claim();
        	if(claimRepository.existsByPolicyNo(policyNo))
        	{
        		return "You have already applied for the claim please check your mail for updates..................";
        	}
        	
        		claim.setEmail(email);
                claim.setPolicyName(policyName);
                claim.setPolicyNo(policyNo);
                claim.setPolicyId(policyId);
                claim.setDescription(description);

                claim.setStatus(Status.PENDING.toString());
                if (!file.isEmpty()) {
                    byte[] photoBytes = file.getBytes();
                    Blob photoBlob = new SerialBlob(photoBytes);
                    claim.setImage(photoBlob);
                }

                claimRepository.save(claim);
                return "Thank You For Requesting Claim We Will Get Back To You Please Check Email for Future Updates............ ";

        	
                    } catch (IOException e) {
            e.printStackTrace();
            return "Failed to save claim: " + e.getMessage();
        }
    }

    private byte[] getPhotoBytes(Blob photoBlob) throws SQLException {
        if (photoBlob != null) {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    
    public List<ClaimDto> getAllClaims() throws Exception {
        List<Claim> allClaims = claimRepository.findAll();
        List<ClaimDto> responses = new ArrayList<>();
        for (Claim claimDetails : allClaims) {
            byte[] photoBytes = getPhotoBytes(claimDetails.getImage());
            String base64Photo = (photoBytes != null && photoBytes.length > 0) ? Base64.encodeBase64String(photoBytes) : null;

            ClaimDto claimDTO = new ClaimDto();
            claimDTO.setEmail(claimDetails.getEmail());
            claimDTO.setPolicyNo(claimDetails.getPolicyNo());
            claimDTO.setPolicyId(claimDetails.getPolicyId());
            claimDTO.setDescription(claimDetails.getDescription());

            claimDTO.setPolicyName(claimDetails.getPolicyName());
            claimDTO.setStatus(claimDetails.getStatus());
            claimDTO.setImage(base64Photo);

            responses.add(claimDTO);
        }
        return responses;
    }

    
    public String claimApproval(String email) {
        List<Claim> claims = claimRepository.findByEmail(email);
        if (!claims.isEmpty()) {
            for (Claim claim : claims) {
                claim.setStatus(Status.APPROVED.toString());
                claim.setUpdateDate(LocalDateTime.now());
                claimRepository.save(claim);

                String subject = "Claim Approved";
                String text = "Your claim for policy " + claim.getPolicyName() + "  with your policy number" + claim.getPolicyNo() +" has been approved.";
                 try {
                    emailService.sendMail(claim.getEmail(), subject, text);
                 } catch (Exception e) {
                     logger.error("Failed to send claim approval email", e);
                 }
            }
            return "Status Updated";
        } else {
            return "Claim not found";
        }
    }

    
    public String cancelApproval(String email) {
        List<Claim> claims = claimRepository.findByEmail(email);
        if (!claims.isEmpty()) {
            for (Claim claim : claims) {
                claim.setStatus(Status.REJECTED.toString());
                claim.setUpdateDate(LocalDateTime.now());
                claimRepository.save(claim);
                String subject = "Claim Approved";
                String text = "Your claim for policy " + claim.getPolicyName() + "  with your policy number" + claim.getPolicyNo() +" has been Rejected Please provide valid details to claim your policy.";
                 try {
                    emailService.sendMail(claim.getEmail(), subject, text);
                 } catch (Exception e) {
                     logger.error("Failed to send claim approval email", e);
                 }
            }
            return "Status Updated";
        } else {
            return "Claim not found";
        }
    }

    
    public List<Claim> getByEmail(String email) {
        return claimRepository.findByEmail(email);
    }

    
    public List<ClaimDto> getByStatus(String status) throws SQLException {
        
        List<Claim> claims = claimRepository.findAllByStatus(status);
        List<ClaimDto> claimDTOs = new ArrayList<>();

        for (Claim claim : claims) {
            ClaimDto cdto = new ClaimDto();
            
            cdto.setPolicyName(claim.getPolicyName());
            cdto.setPolicyNo(claim.getPolicyNo());
            cdto.setPolicyId(claim.getPolicyId());
           cdto.setDescription(claim.getDescription());

            cdto.setEmail(claim.getEmail());
            cdto.setStatus(claim.getStatus());
            String base64Photo = Base64.encodeBase64String(claim.getImage().getBytes(1, (int) claim.getImage().length()));
            cdto.setImage(base64Photo);
            claimDTOs.add(cdto);
        }
        return claimDTOs;

    }
    
    public List<ClaimDto> getUserClaims(String email) throws SQLException
    {
    	List<Claim> claim = getByEmail(email);
        List<ClaimDto> claimDTOs = new ArrayList<>();
    
        for (Claim claims : claim) {
            ClaimDto cdto = new ClaimDto();
            cdto.setPolicyName(claims.getPolicyName());
            cdto.setPolicyId(claims.getPolicyId());
            cdto.setPolicyNo(claims.getPolicyNo());
            cdto.setEmail(email);
            cdto.setStatus(claims.getStatus());
             claimDTOs.add(cdto);
        }
        return claimDTOs;
      
    }
    
    public List<ClaimDto> getMyAllClaims(String email) throws Exception {
        List<Claim> allClaims = claimRepository.findAllByEmail(email);
        List<ClaimDto> responses = new ArrayList<>();
        for (Claim claimDetails : allClaims) {
            byte[] photoBytes = getPhotoBytes(claimDetails.getImage());
            String base64Photo = (photoBytes != null && photoBytes.length > 0) ? Base64.encodeBase64String(photoBytes) : null;

            ClaimDto claimDTO = new ClaimDto();
            claimDTO.setEmail(claimDetails.getEmail());
            claimDTO.setPolicyNo(claimDetails.getPolicyNo());
            claimDTO.setPolicyId(claimDetails.getPolicyId());
            claimDTO.setDescription(claimDetails.getDescription());

            claimDTO.setPolicyName(claimDetails.getPolicyName());
            claimDTO.setStatus(claimDetails.getStatus());
            claimDTO.setImage(base64Photo);

            responses.add(claimDTO);
        }
        return responses;
    }
    
    
   }