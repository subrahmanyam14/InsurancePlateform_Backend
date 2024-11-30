package com.learner.dto;

import com.learner.entity.UserInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoDto {
	
	private int id;
	private String firstname;
	private String lastname;
	private String dob;
	private String email;
	private String phno;
	private String password;
	private String gender;
	private String address;
	private String adhaarPan;
	private String state;
	private String pincode;

}
