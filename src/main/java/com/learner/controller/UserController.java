package com.learner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learner.dto.AllMyPolicies;
import com.learner.dto.LoginRequest;
import com.learner.dto.LoginResponse;
import com.learner.dto.PasswordRequest;
import com.learner.dto.UserInfoDto;
import com.learner.exception.UserRelavantException;
import com.learner.securityService.JwtService;
import com.learner.service.HealthInsuranceServiceImpl;
import com.learner.service.HomeInsuranceServiceImpl;
import com.learner.service.LifeInsuranceServiceImpl;
import com.learner.service.OtpService;
import com.learner.service.TravelInsuranceServiceImpl;
import com.learner.service.UserInfoService;
import com.learner.service.VehicleInsuranceServiceImpl;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:5173", "https://vts-project.vercel.app/"}) 
public class UserController {
	
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private TravelInsuranceServiceImpl travelInsuranceServiceImpl;
	@Autowired
	private HomeInsuranceServiceImpl homeInsuranceServiceImpl;
	@Autowired
	private LifeInsuranceServiceImpl lifeInsuranceServiceImpl;
	@Autowired
	private HealthInsuranceServiceImpl healthInsuranceServiceImpl;
	@Autowired
	private VehicleInsuranceServiceImpl vehicleInsuranceServiceImpl;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private OtpService otpService;  
	@Autowired 
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/register")
	public ResponseEntity<UserInfoDto> registerUser(@RequestBody UserInfoDto req)
	{
		return new ResponseEntity<>(userInfoService.saveUserInfo(req), HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest req)
	{
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())); 
        if (authentication.isAuthenticated()) {
        	LoginResponse res = new LoginResponse(jwtService.generateToken(req.getEmail()), userInfoService.getUsernameByEmail(req.getEmail()), userInfoService.getRoleByEmail(req.getEmail()),  "Authentication Successfull...");
        	return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
       } else { 
            throw new UserRelavantException("invalid user request !"); 
        } 
	}
	
	@GetMapping("/get-profile")
	public ResponseEntity<UserInfoDto> getProfile(@RequestHeader("Authorization")  String authorizationHeader)
	{
		if(authorizationHeader == null && !authorizationHeader.startsWith("Bearer "))
		{
			 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String token = authorizationHeader.substring(7);
		String email = jwtService.extractUsername(token);
		return new ResponseEntity<>(userInfoService.getUserByEmail(email), HttpStatus.OK);
	}
	
	@PutMapping("/update-profile")
	public ResponseEntity<UserInfoDto> updateProfile(@RequestBody UserInfoDto req)
	{
		return new ResponseEntity<>(userInfoService.updateUser(req), HttpStatus.OK); 
		
	}
	
	@PatchMapping("/update-password")
	public ResponseEntity<String> updatePassword(@RequestBody PasswordRequest req, @RequestHeader("Authorization") String authorizationHeader)
	{
		if(authorizationHeader == null && !authorizationHeader.startsWith("Bearer "))
		{
			 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String token = authorizationHeader.substring(7);
		String email = jwtService.extractUsername(token);
		userInfoService.changePassword(email, req);
		return new ResponseEntity<>("Your Password Successfully reset.", HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/admin/add-admin")
	public ResponseEntity<UserInfoDto> addAdmin(@RequestBody UserInfoDto req)
	{
		return new ResponseEntity<>(userInfoService.saveAdmin(req), HttpStatus.ACCEPTED);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/admin/add-agent")
	public ResponseEntity<UserInfoDto> addAgent(@RequestBody UserInfoDto req)
	{
		return new ResponseEntity<>(userInfoService.saveAdmin(req), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/admin/get-all-users")
	public ResponseEntity<List<UserInfoDto>> getAllUsers()
	{
		return new ResponseEntity<>(userInfoService.getAllUsers(), HttpStatus.OK);
	}
	
	@GetMapping("/get-all-my-policies")
	public ResponseEntity<AllMyPolicies> getAllMyPolicies(@RequestHeader("Authorization")  String authorizationHeader)
	{
		String email = userInfoService.getEmailFromToken(authorizationHeader);
		AllMyPolicies all = new AllMyPolicies();
		all.setHealthInsuranceDTO(healthInsuranceServiceImpl.mypolicy(email));
		all.setHomeInsuranceDTO(homeInsuranceServiceImpl.mypolicy(email));
		all.setLifeInsuranceDTO(lifeInsuranceServiceImpl.getmypolicy(email));
		all.setTravelInsuranceDTO(travelInsuranceServiceImpl.getUserPolicy(email));
		all.setVehicleInsuranceDto(vehicleInsuranceServiceImpl.mypolicy(email));
		return ResponseEntity.ok(all);
	}
	

}
