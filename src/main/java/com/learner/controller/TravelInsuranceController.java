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

import com.learner.dto.TravelInsuranceDTO;
import com.learner.service.TravelInsuranceServiceImpl;
import com.learner.service.UserInfoService;

@RestController
@RequestMapping("/travelinsurance")
@CrossOrigin(origins = {"http://localhost:5173", "https://vts-project.vercel.app/"})
public class TravelInsuranceController {

    @Autowired
    TravelInsuranceServiceImpl travelInsuranceService;
    @Autowired
    private UserInfoService userInfoService;
    
    @PostMapping("/apply")
    public ResponseEntity<TravelInsuranceDTO> travelInsurance(@RequestHeader("Authorization")  String authorizationHeader,
                                  @RequestParam("policyId") String policyId,
                                  @RequestParam("policyName") String policyName,
                                  @RequestParam("destination") String destination,
                                  @RequestParam("organisation") String organisation,
                                  @RequestParam("startTime") String startTime,
                                  @RequestParam("endTime") String endTime,
                                  @RequestParam("modeOfTravel") String modeOfTravel,
                                  @RequestParam("ticketId") String ticketId,
                                  @RequestParam("nomineeName") String nomineeName,
                                  @RequestParam("nomineeRelation") String nomineeRelation,
                                  @RequestParam("nomineeAge") String nomineeAge,
                                  @RequestParam("nomineeAadharNo") String nomineeAadharNo,
                                  @RequestParam("documentimage") MultipartFile documentimage
                                  )throws IOException, SQLException
                              {
    	String email = userInfoService.getEmailFromToken(authorizationHeader);
    	TravelInsuranceDTO message = travelInsuranceService.ApplyTravelInsurance(documentimage, email,  policyId,policyName,destination,organisation,startTime,
                endTime,modeOfTravel,ticketId, nomineeName, nomineeRelation, nomineeAge,nomineeAadharNo);
        return ResponseEntity.ok(message);
   
}
    

    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @GetMapping("/get-all-applications")
    public ResponseEntity<List<TravelInsuranceDTO>> getAll()
    {
        return ResponseEntity.ok(travelInsuranceService.AllApplications());
    }

    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @PutMapping("/approve/{policyNo}")
    public ResponseEntity<String> StatusUpdation(@PathVariable String policyNo ,  @RequestParam("status") String status)
    {
        return ResponseEntity.ok(travelInsuranceService.statusApproval(policyNo, status));
    }
    
    @GetMapping("/get-my-policy")
    public ResponseEntity<TravelInsuranceDTO> insurance(@RequestHeader("Authorization")  String authorizationHeader)
    {
    	String email = userInfoService.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(travelInsuranceService.getUserPolicy(email));
    }
    @GetMapping("/get-policy-by-policyNo/{policyNo}")
    public ResponseEntity<TravelInsuranceDTO> getPolicyByPolicyNo(@PathVariable String policyNo)
    {
    	return ResponseEntity.ok(travelInsuranceService.getPolicyByPolicyno(policyNo));
    }
    
}