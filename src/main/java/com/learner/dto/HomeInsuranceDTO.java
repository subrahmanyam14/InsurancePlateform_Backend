package com.learner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeInsuranceDTO {

    private Long id;
    private String email;
    private String type;
    private String policyNo;
    private String policyId;
    private String policyName;
    private String houseno;
    private String owner;
    private String location; 
    private String documentimage;
    private String status;
    
}
