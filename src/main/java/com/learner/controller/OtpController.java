package com.learner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learner.dto.ForgotPasswordReq;
import com.learner.service.OtpService;
import com.learner.service.UserInfoService;

@RestController
@RequestMapping("/otp")
@CrossOrigin(origins = {"http://localhost:5173", "https://vts-project.vercel.app/"})
public class OtpController {
	
	@Autowired
	private UserInfoService infoService;
	@Autowired
	private OtpService otpService;
	
	@PostMapping("/generate/{email}")
	public ResponseEntity<String > generateOpt(@PathVariable String email)
	{
		String otp = otpService.generateOtp(email);
		return ResponseEntity.ok(otp);

	}
	
	@PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody ForgotPasswordReq req) {
		
        boolean isValid = otpService.verifyOtp(req.getEmail(), req.getOtp());
        if (isValid) {
        	otpService.resetPassword(req.getEmail(), req.getPassword());      	
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.status(400).body("Invalid or expired OTP.");
        }
    }
}
