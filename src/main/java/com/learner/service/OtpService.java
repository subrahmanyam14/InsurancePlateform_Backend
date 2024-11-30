package com.learner.service;

import java.time.Instant;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learner.config.PassEncoder;
import com.learner.entity.Otp;
import com.learner.entity.UserInfo;
import com.learner.exception.OtpRelaventException;
import com.learner.exception.UserRelavantException;
import com.learner.repository.OtpRepository;
import com.learner.repository.UserInfoRepository;

@Service
public class OtpService {
	
	@Autowired
	private OtpRepository otpRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;
	 
	@Autowired
	private PasswordEncoder encoder;
	
	private static final long OTP_VALID_DURATION = 10 * 60 * 1000;
	
	
	public String generateOtp(String email)
	{
		
		String otp = String.valueOf(new Random().nextInt(999999));
		if(userInfoRepository.existsByEmail(email))
		{
			Otp otpEntity = new Otp();
			if(otpRepository.existsByEmail(email))
			{
				 otpEntity = otpRepository.findByEmail(email).orElseThrow(
						() -> new OtpRelaventException("Otp not found with email  "+email)
						);
			}
			otpEntity.setEmail(email);
			otpEntity.setExpiryTime(Instant.now().toEpochMilli() + OTP_VALID_DURATION);
			otpEntity.setOtp(otp);
			otpRepository.save(otpEntity);
			sendOtpEmail(email, otp);
			return otp;
		}
		else
		{
			throw new UserRelavantException("user not found with mail "+email);
		}
		
	}
	
	private void sendOtpEmail(String to, String otp)
	{
		SimpleMailMessage message = new  SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Your OTP Code");
		message.setText("Your otp code is "+otp+". And it will expire with in 10 mins. So please use this otp before expiration.");
		javaMailSender.send(message);
	}
	
	public boolean verifyOtp(String email, String otp)
	{
		Otp otpEntity = otpRepository.findByEmail(email).orElseThrow(
				() -> new OtpRelaventException("Otp not found with email  "+email)
				);
		if(!otpEntity.getOtp().equals(otp))
		{
			
			return false;
		}
		if(otpEntity.getExpiryTime() < Instant.now().toEpochMilli())
		{
			return false;
			
		}
		
		otpRepository.delete(otpEntity);
		return true;
	}

	
	public void resetPassword(String email, String password)
	{
		UserInfo user = userInfoRepository.findByEmail(email).orElseThrow(
				() -> new UserRelavantException("User not found with email "+email)
				);
		user.setPassword(encoder.encode(password));
		userInfoRepository.save(user);
	}
}
