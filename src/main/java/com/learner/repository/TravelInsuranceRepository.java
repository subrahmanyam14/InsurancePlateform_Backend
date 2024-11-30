package com.learner.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learner.entity.TravelInsuranceDetails;

@Repository
public interface TravelInsuranceRepository extends  JpaRepository<TravelInsuranceDetails, Long> {

	Optional<TravelInsuranceDetails> findByEmail(String email);

	boolean existsByEmail(String email);

	Optional<TravelInsuranceDetails> findByPolicyNo(String policyNo);

}
