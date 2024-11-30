package com.learner.controller;

import java.sql.SQLException;
import java.util.List;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learner.dto.ClaimDto;
import com.learner.entity.Claim;
import com.learner.service.ClaimServiceImpl;
import com.learner.service.UserInfoService;






@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://vts-insurance-plateform.netlify.app", "https://vts-project.vercel.app/"})
@RequestMapping("/claim")
public class ClaimController {
    @Autowired
    private ClaimServiceImpl claimServices;
    @Autowired
    private UserInfoService userInfoService;

    
    @PostMapping("/add-claim")
    public ResponseEntity<String> createClaim(@RequestParam("file") MultipartFile file,
                              @RequestParam("policyName") String policyName,
                              @RequestParam("policyId") String policyId,
                              @RequestParam("policyNo") String policyNo,
                              @RequestParam("description") String description,

                              @RequestHeader("Authorization")  String authorizationHeader) throws SerialException, SQLException {
    	String email = userInfoService.getEmailFromToken(authorizationHeader);
        String message = claimServices.saveClaims(email, policyName, policyId,policyNo,description, file);
        return ResponseEntity.ok(message);
    }

    
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
    @GetMapping("/get-all-claims")
    public ResponseEntity<List<ClaimDto>> getAllClaims() throws Exception {
        return ResponseEntity.ok(claimServices.getAllClaims());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
    @PutMapping("/approve-claim/{email}")
    public ResponseEntity<String> approveClaim(@PathVariable String email) {
    	
        return new ResponseEntity<>(claimServices.claimApproval(email), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
    @PutMapping("/cancel-claim/{email}")
    public ResponseEntity<String> cancelClaim(@PathVariable String email) {
        return new ResponseEntity<>(claimServices.cancelApproval(email), HttpStatus.OK);
    }

    @GetMapping("/get-user-claims")
    public ResponseEntity<List<ClaimDto>> getByEmail(@RequestHeader("Authorization") String authorizationHeader) throws SQLException {
    	String email = userInfoService.getEmailFromToken(authorizationHeader);
        
    
         return new ResponseEntity<>(claimServices.getUserClaims(email), HttpStatus.OK);
    }
  

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_AGENT')")
    @GetMapping("/get-all-users-status")
    public ResponseEntity<List<ClaimDto>> getApprovedClaims(@RequestParam("status") String status) throws SQLException {
        
        return new ResponseEntity<>(claimServices.getByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/get-my-claims")
    public ResponseEntity<List<ClaimDto>> getMyClaims(@RequestHeader("Authorization") String authorizationHeader) throws Exception
    {
    	String email = userInfoService.getEmailFromToken(authorizationHeader);
    	return ResponseEntity.ok(claimServices.getMyAllClaims(email));
    }

}