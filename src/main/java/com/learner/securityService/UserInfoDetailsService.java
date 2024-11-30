package com.learner.securityService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.learner.entity.UserInfo;
import com.learner.exception.UserRelavantException;
import com.learner.repository.UserInfoRepository;

@Service
public class UserInfoDetailsService  implements UserDetailsService{
	
	@Autowired
	private UserInfoRepository userInfoRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<UserInfo> userDetail = userInfoRepository.findByEmail(email);
		return userDetail.map(UserInfoDetails::new) 
				.orElseThrow(() -> new UserRelavantException("User not found with email " + email));
	}

}
