package com.learner.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learner.dto.PasswordRequest;
import com.learner.dto.UserInfoDto;
import com.learner.entity.Role;
import com.learner.entity.UserInfo;
import com.learner.exception.UserRelavantException;
import com.learner.repository.UserInfoRepository;
import com.learner.securityService.JwtService;

@Service
public class UserInfoService {
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private JwtService jwtService;
	 @Autowired
	    private EmailService emailService;
	
	private UserInfoDto toDto(UserInfo req)
	{
		UserInfoDto dto = new UserInfoDto();
		dto.setId(req.getId());
		dto.setFirstname(req.getFirstname());
		dto.setLastname(req.getLastname());
		dto.setPassword("");
		dto.setAddress(req.getAddress());
		dto.setDob(req.getDob());
		dto.setAdhaarPan(req.getAdhaarPan());
		dto.setEmail(req.getEmail());
		dto.setPhno(req.getPhno());
		dto.setPincode(req.getPincode());
		dto.setState(req.getState());
		dto.setGender(req.getGender());
		return dto;
	}
	private UserInfo toEntity(UserInfoDto req)
	{
		UserInfo user = new UserInfo();
		user.setFirstname(req.getFirstname());
		user.setLastname(req.getLastname());
		user.setPassword(req.getPassword());
		user.setAddress(req.getAddress());
		user.setDob(req.getDob());
		user.setAdhaarPan(req.getAdhaarPan());
		user.setEmail(req.getEmail());
		user.setPhno(req.getPhno());
		user.setPincode(req.getPincode());
		user.setState(req.getState());
		user.setGender(req.getGender());		
		return user;
	}
	
	
	
	public UserInfoDto saveUserInfo(UserInfoDto dto)
	{
		if(userInfoRepository.existsByEmail(dto.getEmail()) || userInfoRepository.existsByAdhaarPan(dto.getAdhaarPan()))
		{
			throw new UserRelavantException("User already exists..!");
		}
		UserInfo user = toEntity(dto);
		user.setRole(Role.ROLE_USER.toString());
		user.setPassword(encoder.encode(user.getPassword()));
		user = userInfoRepository.save(user);
		 String subject = "You have registered successfuly";
         String text = "Your registeration is successfully done.";
         try {
             emailService.sendMail(user.getEmail(), subject, text);
         } catch (MailSendException e) {
             //logger.error("Failed to send approval confirmation email due to mail server connection issue", e);
         } catch (Exception e) {
             //logger.error("Failed to send booking confirmation email", e);
         }
		return toDto(user);
	}
	
	public UserInfoDto saveAdmin(UserInfoDto dto)
	{
		if(userInfoRepository.existsByEmail(dto.getEmail()) || userInfoRepository.existsByAdhaarPan(dto.getAdhaarPan()))
		{
			throw new UserRelavantException("User already exists..!");
		}
		UserInfo user = toEntity(dto);
		user.setRole(Role.ROLE_ADMIN.toString());
		user.setPassword(encoder.encode(user.getPassword()));
		user = userInfoRepository.save(user);
		return toDto(user);
	}
	
	public void saveAdminByDefault(String email, String adhaarPan, String password)
	{
		if(!(userInfoRepository.existsByEmail(email) || userInfoRepository.existsByAdhaarPan(adhaarPan)))
		{
		UserInfo admin = new UserInfo();
		admin.setEmail(email);
		admin.setAdhaarPan(adhaarPan);
		admin.setPassword(encoder.encode(password));
		admin.setRole(Role.ROLE_ADMIN.toString());
		userInfoRepository.save(admin);
		}
	}
	public void saveAgentByDefault(String email, String adhaarPan, String password)
	{
		if(!(userInfoRepository.existsByEmail(email) || userInfoRepository.existsByAdhaarPan(adhaarPan)))
		{
		UserInfo admin = new UserInfo();
		admin.setEmail(email);
		admin.setAdhaarPan(adhaarPan);
		admin.setPassword(encoder.encode(password));
		admin.setRole(Role.ROLE_AGENT.toString());
		userInfoRepository.save(admin);
		}
	}
	
	public UserInfoDto saveAgent(UserInfoDto dto)
	{
		if(userInfoRepository.existsByEmail(dto.getEmail()) || userInfoRepository.existsByAdhaarPan(dto.getAdhaarPan()))
		{
			throw new UserRelavantException("User already exists..!");
		}
		UserInfo user = toEntity(dto);
		user.setRole(Role.ROLE_AGENT.toString());
		user.setPassword(encoder.encode(user.getPassword()));
		user = userInfoRepository.save(user);
		return toDto(user);
	}
	
	public UserInfoDto updateUser(UserInfoDto dto)
	{
		
		UserInfo user = userInfoRepository.findById(dto.getId()).orElseThrow(
				() -> new UserRelavantException("User not found with id "+dto.getId())
				);
		
		if(dto.getAddress() != null)
		{
			user.setAddress(dto.getAddress());
		} 
		if(dto.getDob() != null)
		{
			user.setDob(dto.getDob());
		}
		if(dto.getFirstname() != null)
		{
			user.setFirstname(dto.getFirstname());
		}
		if(dto.getGender() != null)
		{
			user.setGender(dto.getGender());
		}
		if(dto.getLastname() != null)
		{
			user.setLastname(dto.getLastname());
		}
		if(dto.getPhno() != null)
		{
			user.setPhno(dto.getPhno());
		}
		if(dto.getPincode() != null)
		{
			user.setPincode(dto.getPincode());
		}
		if(dto.getState() != null)
		{
			user.setState(dto.getState());
		}
		return toDto(userInfoRepository.save(user));
	}
	
	public void changePassword(String email, PasswordRequest req)
	{
		UserInfo user = userInfoRepository.findByEmail(email).orElseThrow(
				() -> new UserRelavantException("User not found with email "+email)
				);
		if(!user.getPassword().equals(encoder.encode(req.getPassword())))
		{
			throw new UserRelavantException("Given password is not matched with the previous password.");
		}
		user.setPassword(encoder.encode(req.getNewPassword()));
		userInfoRepository.save(user);
		
	}
	
	public UserInfoDto getUserByEmail(String email)
	{

		UserInfo user = userInfoRepository.findByEmail(email).orElseThrow(
				() -> new UserRelavantException("User not found with email "+email)
				);
		return toDto(user);
		
	}
	
	public List<UserInfoDto> getAllUsers()
	{
		List<UserInfo> users = userInfoRepository.findAll();
		List<UserInfoDto> response = new ArrayList<UserInfoDto>();
		for(UserInfo user : users)
		{
			response.add(toDto(user));
		}
		return response;
	}
	
	public String getEmailFromToken(String authorizationHeader)
	{
		if(authorizationHeader == null && !authorizationHeader.startsWith("Bearer "))
		{
			 throw new UserRelavantException("Authorization Header  found not error");
		}
		String token = authorizationHeader.substring(7);
		return jwtService.extractUsername(token);
	}
	
	public String getRoleByEmail(String email)
	{
		UserInfo user = userInfoRepository.findByEmail(email).orElseThrow(
				() -> new UserRelavantException("User not found with email "+email)
				);
		return user.getRole();
	}
	
	public String getUsernameByEmail(String email)
	{
		UserInfo user = userInfoRepository.findByEmail(email).orElseThrow(
				() -> new UserRelavantException("User not found with email "+email)
				);
		return user.getFirstname()+user.getLastname();
	}
	
	
}
