package com.learner.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.learner.securityService.JwtAuthFilter;
import com.learner.securityService.UserInfoDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoDetailsService();
    }
    

    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		return http.csrf().disable().cors()
				.and().authorizeHttpRequests()
				.requestMatchers("/user/register", "/user/login", "/otp/verify", "/otp/generate/**")
				.permitAll().and().authorizeHttpRequests()
				.requestMatchers("/policy/get-all-policies")
				.permitAll().and()
				.authorizeHttpRequests()
				.requestMatchers( "/healthinsurance/apply", "/healthinsurance/get-all-applications", "/healthinsurance/approve/**", "/healthinsurance/get-my-policy", "/healthinsurance/get-policy-by-policyNo/**")
				.authenticated().and()
				.authorizeHttpRequests()
				.requestMatchers( "/vehicleinsurance/apply", "/vehicleinsurance/get-all-applications", "/vehicleinsurance/approve/**", "/vehicleinsurance/get-my-policy", "/vehicleinsurance/get-policy-by-policyNo/**")
				.authenticated().and()
				.authorizeHttpRequests()
				.requestMatchers( "/travelinsurance/apply", "/travelinsurance/get-all-applications", "/travelinsurance/approve/**", "/travelinsurance/get-my-policy", "/travelinsurance/get-policy-by-policyNo/**")
				.authenticated().and()
				.authorizeHttpRequests()
				.requestMatchers( "/homeinsurance/apply", "/homeinsurance/get-all-applications", "/homeinsurance/approve/**", "/homeinsurance/get-my-policy", "/homeinsurance/get-policy-by-policyNo/**")
				.authenticated().and()
				.authorizeHttpRequests()
				.requestMatchers( "/lifeinsurance/apply", "/lifeinsurance/get-all-applications", "/lifeinsurance/get-my-policy", "/lifeinsurance/approve/**", "/lifeinsurance/get-policy-by-policyNo/**")
				.authenticated().and()
				.authorizeHttpRequests()
				.requestMatchers( "/claim/get-user-claims", "/claim/add-claim", "/claim/get-all-users-status", "/claim/get-all-claims", "/claim/get-my-claims", "/claim/approve-claim/**", "/claim/cancel-claim/**")
				.authenticated().and()
				.authorizeHttpRequests()
				.requestMatchers( "/payment/save-payment", "/payment/get-all-payments", "/payment/get-my-payments", "/payment/get-specific-payments/**")
				.authenticated().and()
				.authorizeHttpRequests()
				.requestMatchers( "/policy/add-policy", "/policy/update-policy", "/policy/delete-policy/**")
				.authenticated().and()
				.authorizeHttpRequests()
				.requestMatchers("/user/get-profile", "/user/get-all-my-policies", "/user/update-profile", "/user/update-password", "/user/admin/**").authenticated()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
    
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
