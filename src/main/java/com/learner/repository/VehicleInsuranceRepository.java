package com.learner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learner.entity.TravelInsuranceDetails;
import com.learner.entity.VehicleInsuranceDetails;

@Repository
public interface VehicleInsuranceRepository extends JpaRepository<VehicleInsuranceDetails, Long>{

	Optional<VehicleInsuranceDetails> findByEmail(String email);

	Optional<VehicleInsuranceDetails> findByPolicyNo(String id);

	boolean existsByEmail(String email);

}
