package com.learner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthInsuranceDTO {

    private Long id;
    private String email;
    private String type;
    private String policyNo;
    private String policyId;
    private String policyName;
    private String existing_medical_condition;
    private String current_medication;    
    private String documentimage;
    private String status;
    
}
