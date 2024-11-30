package com.learner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learner.entity.Claim;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByEmail(String email);
    List<Claim> findAllByStatus(String status);
	List<Claim> findAllByEmail(String email);
	boolean existsByPolicyNo(String policyNo);
}
