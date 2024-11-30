package com.learner.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learner.entity.LifeInsurance;

@Repository
public interface LifeInsuranceRepository extends JpaRepository<LifeInsurance, Long>{

	Optional<LifeInsurance> findByEmail(String email);

	boolean existsByEmail(String email);

	Optional<LifeInsurance> findByPolicyNo(String policyNo);

}