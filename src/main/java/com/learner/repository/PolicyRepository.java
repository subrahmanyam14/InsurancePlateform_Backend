package com.learner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learner.entity.Policies;

@Repository
public interface PolicyRepository extends JpaRepository<Policies, Long>{

}
