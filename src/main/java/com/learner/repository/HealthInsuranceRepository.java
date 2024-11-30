package com.learner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learner.entity.HealthInsuranceDetails;

@Repository
public interface HealthInsuranceRepository extends  JpaRepository<HealthInsuranceDetails, Long> {

	boolean existsByEmail(String email);

	Optional<HealthInsuranceDetails> findByEmail(String email);

	Optional<HealthInsuranceDetails> findByPolicyNo(String policyNo);

}