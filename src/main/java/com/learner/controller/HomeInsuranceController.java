package com.learner.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
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
import com.learner.dto.LifeInsuranceDTO;
import com.learner.service.HomeInsuranceServiceImpl;
import com.learner.service.UserInfoService;


@RestController
@RequestMapping("homeinsurance")
@CrossOrigin(origins = {"http://localhost:5173", "https://vts-project.vercel.app/"})
public class HomeInsuranceController {
    @Autowired
    private HomeInsuranceServiceImpl homeInsuranceService;
    @Autowired
    private UserInfoService userInfoService;
    @PostMapping("/apply")
    public ResponseEntity<HomeInsuranceDTO> ApplyHomeInsurance(@RequestParam("documentimage") MultipartFile documentimage ,
    @RequestHeader("Authorization") String authorizationHeader ,
    @RequestParam("policyId") String policyId,  
    @RequestParam("policyName") String policyName,  
    @RequestParam("houseno") String houseno, 
    @RequestParam("owner")String owner, 
    @RequestParam("location")String location)throws IOException, SQLException
    {
    	String email = userInfoService.getEmailFromToken(authorizationHeader);
       return ResponseEntity.ok(homeInsuranceService.ApplyHomeInsurance(documentimage, email, policyId,policyName, location, houseno, owner));
    }

    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @GetMapping("/get-all-applications")
    public ResponseEntity<List<HomeInsuranceDTO>> getAll()
    {
        return ResponseEntity.ok(homeInsuranceService.AllApplications());
    }

    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @PutMapping("/approve/{policyNo}")
    public ResponseEntity<String> StatusUpdation(@PathVariable String policyNo,  @RequestParam("status") String status)
    {
        return ResponseEntity.ok(homeInsuranceService.statusApproval(policyNo, status));
    }
    @GetMapping("/get-my-policy")
    public  ResponseEntity<HomeInsuranceDTO> getmypolicy(@RequestHeader("Authorization")  String authorizationHeader)
    {
    	String email = userInfoService.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok( homeInsuranceService.mypolicy(email));
    }
    
    @GetMapping("/get-policy-by-policyNo/{policyNo}")
    public ResponseEntity<HomeInsuranceDTO> getPolicyByPolicyNo(@PathVariable String policyNo)
    {
    	return ResponseEntity.ok(homeInsuranceService.getPolicyByPolicyno(policyNo));
    }
}