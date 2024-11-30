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

import com.learner.dto.VehicleInsuranceDto;
import com.learner.service.UserInfoService;
import com.learner.service.VehicleInsuranceServiceImpl;


@RestController
@RequestMapping("/vehicleinsurance")
@CrossOrigin(origins = {"http://localhost:5173", "https://vts-project.vercel.app/"})
public class VehicleInsuranceController {
    @Autowired
    private VehicleInsuranceServiceImpl vehicleInsuranceService;
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/apply")
    public ResponseEntity<VehicleInsuranceDto> ApplyVehicleInsurance(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("policyId") String policyId, @RequestParam("documentimage") MultipartFile documentimage ,@RequestParam("policyName") String policyName,@RequestParam("vehicleNumber") String vehicleNumber,@RequestParam("vehicleCompany") String vehicleCompany,@RequestParam("vehicleModel") String vehicleModel,@RequestParam("chassisNumber") String chassisNumber,@RequestParam("manufacturingYear") String manufacturingYear) throws IOException, SQLException
    {
    	String email = userInfoService.getEmailFromToken(authorizationHeader);
       return ResponseEntity.ok(vehicleInsuranceService.ApplyVehicleInsurance(documentimage, email, vehicleNumber, vehicleCompany, vehicleModel, chassisNumber, manufacturingYear,policyName,policyId));
    }

    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @GetMapping("/get-all-applications")
    public ResponseEntity<List<VehicleInsuranceDto>> getAll()
    {
        return ResponseEntity.ok(vehicleInsuranceService.AllApplications());
    }

    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @PutMapping("/approve/{policyId}")
    public ResponseEntity<String> StatusUpdation(@PathVariable("policyId") String id,  @RequestParam("status") String status)
    {
        return ResponseEntity.ok(vehicleInsuranceService.statusApproval(id, status));
    }
    
    @GetMapping("/get-my-policy")
    public ResponseEntity<VehicleInsuranceDto> myPolicy(@RequestHeader("Authorization")  String authorizationHeader)
    {
    	String email = userInfoService.getEmailFromToken(authorizationHeader);
    	return ResponseEntity.ok(vehicleInsuranceService.mypolicy(email));
    }
    
    @GetMapping("/get-policy-by-policyNo/{policyNo}")
    public ResponseEntity<VehicleInsuranceDto> getPolicyByPolicyNo(@PathVariable String policyNo)
    {
    	return ResponseEntity.ok(vehicleInsuranceService.getPolicyByPolicyno(policyNo));
    }
    
}