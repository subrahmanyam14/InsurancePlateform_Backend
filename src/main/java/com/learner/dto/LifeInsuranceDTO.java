package com.learner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data

public class LifeInsuranceDTO {
    
    private long id;
    private String email;
    private String type;
    private String policyNo;
    private String policyId;
    private String policyName;
    private String nomineeName;
    private String nomineeAge;
    private String nomineeRelation;
    private String nomineeAadharnumber;
    private String status;
}