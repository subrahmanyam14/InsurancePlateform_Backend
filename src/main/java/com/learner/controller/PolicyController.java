package com.learner.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learner.dto.PolicyDto;
import com.learner.service.PolicyServiceImpl;


@RequestMapping("/policy")
@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://vts-project.vercel.app/"})
public class PolicyController {

    @Autowired
    PolicyServiceImpl policyservice;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-policy")
    public ResponseEntity<String> addPolicy(@RequestBody PolicyDto dto) 
    {
        policyservice.savePolicy(dto);
        return new ResponseEntity<>("Policy added successfully", HttpStatus.OK);
    }

    @GetMapping("/get-all-policies")
    public ResponseEntity<List<PolicyDto>> getPolicy()
    {
        return new ResponseEntity<>(policyservice.getAllPolicies(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-policy")
    public ResponseEntity<String> update(@RequestBody PolicyDto dto)
    {
        
        return new ResponseEntity<>(policyservice.updatePolicy(dto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete-policy/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id)
    {
        return new ResponseEntity<>(policyservice.deletePolicy(id), HttpStatus.OK);
       
    }

    
}
