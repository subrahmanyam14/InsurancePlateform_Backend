package com.learner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learner.entity.Otp;

public interface OtpRepository extends JpaRepository<Otp, Integer> {
	
	Optional<Otp> findByEmail(String email);

	boolean existsByEmail(String email);

}
