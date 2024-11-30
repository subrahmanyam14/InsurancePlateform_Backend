package com.learner.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.learner.dto.HealthInsuranceDTO;
import com.learner.dto.HomeInsuranceDTO;
import com.learner.service.HealthInsuranceServiceImpl;
import com.learner.service.UserInfoService;


@RestController
@RequestMapping("/healthinsurance")
@CrossOrigin(origins = {"http://localhost:5173", "https://vts-project.vercel.app/"})
public class HealthInsuranceController {
    @Autowired
    private HealthInsuranceServiceImpl healthInsuranceService;
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/apply")
    public ResponseEntity<HealthInsuranceDTO> ApplyHealthInsurance(@RequestParam("documentimage") MultipartFile documentimage ,
    		@RequestHeader("Authorization")  String authorizationHeader ,
    @RequestParam("policyId") String policyId,
    @RequestParam("policyName") String policyName,
    @RequestParam("existing_medical_condition") String existing_medical_condition,
    @RequestParam("current_medication") String current_medication) throws IOException, SQLException
    {
    	
       String email = userInfoService.getEmailFromToken(authorizationHeader);
       return ResponseEntity.ok(healthInsuranceService.ApplyHealthInsurance(documentimage, email, policyId,policyName, current_medication, current_medication));

    }
    
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @GetMapping("/get-all-applications")
    public ResponseEntity<List<HealthInsuranceDTO>> getAll()
    {
        return ResponseEntity.ok(healthInsuranceService.AllApplications());
    }

    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @PutMapping("/approve/{policyNo}")
    public ResponseEntity<String> StatusUpdation(@PathVariable String policyNo,  @RequestParam("status") String status)
    {
        return ResponseEntity.ok(healthInsuranceService.statusApproval(policyNo, status));
    }
    
    @GetMapping("/get-my-policy")
    public  ResponseEntity<HealthInsuranceDTO> getmypolicy(@RequestHeader("Authorization")  String authorizationHeader)
    {
    	String email = userInfoService.getEmailFromToken(authorizationHeader);
        return  ResponseEntity.ok(healthInsuranceService.mypolicy(email));
    }
    
    @GetMapping("/get-policy-by-policyNo/{policyNo}")
    public ResponseEntity<HealthInsuranceDTO> getPolicyByPolicyNo(@PathVariable String policyNo)
    {
    	return ResponseEntity.ok(healthInsuranceService.getPolicyByPolicyno(policyNo));
    }
    
}