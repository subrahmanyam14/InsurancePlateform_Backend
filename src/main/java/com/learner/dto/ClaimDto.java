package com.learner.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClaimDto {

        private String email;
        private String policyNo;
        private String policyId;
        private String policyName;
        private String description;
        private String status;
        private String image;
        private String date;
    
    }
