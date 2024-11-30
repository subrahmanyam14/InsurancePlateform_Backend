package com.learner.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learner.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer>{
    Optional<Payment> findByPolicyNo(String policyNo);

    List<Payment> findAllByEmail(String email);

	List<Payment> findAllByPolicyNo(String policyNo);
    
}