package com.learner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LifeInsurance {
    
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String email;
private String type = PolicyType.LIFE.toString();
private String policyNo;
private String policyId;
private String policyName;
private String nomineeName;
private String nomineeAge;
private String nomineeRelation;
private String nomineeAadharnumber;
private String status;



}