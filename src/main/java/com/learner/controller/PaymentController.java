package com.learner.controller;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learner.dto.PaymentDto;
import com.learner.service.PaymentService;
import com.learner.service.UserInfoService;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = {"http://localhost:5173", "https://vts-project.vercel.app/"})
public class PaymentController {
    
    @Autowired
    private  PaymentService paymentService;
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/save-payment")
    public ResponseEntity<PaymentDto> savePayment(@RequestHeader("Authorization")  String authorizationHeader ,
    		@RequestParam String policyId, 
    @RequestParam String policyNo, 
    @RequestParam  String amount,
    @RequestParam  String referenceId, 
    @RequestParam String transactionId,
    @RequestParam String policyName,
    @RequestParam MultipartFile file) throws SerialException, IOException, SQLException
    {
    	String email = userInfoService.getEmailFromToken(authorizationHeader);
        return ResponseEntity.ok(paymentService.savePayment(email, amount, policyId, policyNo, policyName, transactionId, referenceId, file));      
    }
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @GetMapping("/get-specific-payments/{policyNo}")
    public ResponseEntity<List<PaymentDto>> getSpecificPayment(@PathVariable("policyNo") String policyNo) throws SQLException
    {
    	
    	return ResponseEntity.ok(paymentService.getSpecificPayment(policyNo));
    	
    }
    
    @PreAuthorize("hasRole('ROLE_AGENT') or hasRole('ROLE_ADMIN')")
    @GetMapping("/get-all-payments")
    public ResponseEntity<List<PaymentDto>> getAllPayments() throws SQLException
    {
    	return ResponseEntity.ok(paymentService.getAllPayments());
    }
    
    @GetMapping("/get-my-payments")
    public ResponseEntity<List<PaymentDto>> getAllPaymentsByMail(@RequestHeader("Authorization") String authorizationHeader ) throws SQLException
    {
    	String email = userInfoService.getEmailFromToken(authorizationHeader);
    	return ResponseEntity.ok(paymentService.getPaymentByEmail(email));
    }
    
}
