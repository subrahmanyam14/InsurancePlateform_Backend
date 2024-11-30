package com.learner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learner.dto.LifeInsuranceDTO;
import com.learner.entity.LifeInsurance;
import com.learner.service.LifeInsuranceServiceImpl;
import com.learner.service.UserInfoService;

@RestController
@RequestMapping("/lifeinsurance")
@CrossOrigin(origins = {"http://localhost:5173", "https://vts-project.vercel.app/"})
public class LifeInsuranceController {
    
   
        @Autowired
        private  LifeInsuranceServiceImpl lifeInsuranceService;
        @Autowired
        private UserInfoService userInfoService; 
        
        @PostMapping("/apply")
        public ResponseEntity<LifeInsuranceDTO> Apply(@RequestBody LifeInsuranceDTO req, @RequestHeader("Authorization")  String authorizationHeader)
        {
        	String email = userInfoService.getEmailFromToken(authorizationHeader);
            return new ResponseEntity<>(lifeInsuranceService.ApplyLifeInsurance(email, req.getPolicyId(), req.getPolicyName(), req.getNomineeName(), req.getNomineeAge(), req.getNomineeRelation(), req.getNomineeAadharnumber()), HttpStatus.OK);
        }

        @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
        @GetMapping("/get-all-applications")
        public ResponseEntity<List<LifeInsurance>> ViewAll()
        {
            return new ResponseEntity<>(lifeInsuranceService.ViewAll(), HttpStatus.OK);
        }
        @GetMapping("/get-my-policy")
        public ResponseEntity<LifeInsuranceDTO> mypolicy(@RequestHeader("Authorization") String authorizationHeader)
        {
        	String email = userInfoService.getEmailFromToken(authorizationHeader);
            return new ResponseEntity<>(lifeInsuranceService.getmypolicy(email), HttpStatus.OK);
        }
        
        @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
        @PutMapping("/approve/{policyNo}")
        public ResponseEntity<String> statusUpdation(@PathVariable String policyNo, @RequestParam("status") String status)
        {
        	lifeInsuranceService.updateStatus(policyNo, status);
        	return new ResponseEntity<>("Status updated successfully.", HttpStatus.OK);
        }
        
        @GetMapping("/get-policy-by-policyNo/{policyNo}")
        public ResponseEntity<LifeInsuranceDTO> getPolicyByPolicyNo(@PathVariable String policyNo)
        {
        	return ResponseEntity.ok(lifeInsuranceService.getPolicyByPolicyno(policyNo));
        }
        
    
    
}