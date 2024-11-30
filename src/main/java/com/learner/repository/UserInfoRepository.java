package com.learner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learner.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

	boolean existsByEmail(String email);

	Optional<UserInfo> findByEmail(String email);

	boolean existsByAdhaarPan(String adhaarPan);

}
