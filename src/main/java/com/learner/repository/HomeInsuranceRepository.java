package com.learner.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learner.entity.HomeInsuranceDetails;

@Repository
public interface HomeInsuranceRepository extends JpaRepository<HomeInsuranceDetails, Long> {

	Optional<HomeInsuranceDetails> findByEmail(String email);

	boolean existsByEmail(String email);

	Optional<HomeInsuranceDetails> findByPolicyNo(String policyNo);
    
}