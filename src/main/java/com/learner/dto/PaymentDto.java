package com.learner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class PaymentDto {
 
    private int id;
    private String policyId;
    private String policyNo;
    private String policyName;
    private String amount;
    private String referenceId;
    private String transactionId;
    private String email;
    private String imageProof;
    private String date;

}