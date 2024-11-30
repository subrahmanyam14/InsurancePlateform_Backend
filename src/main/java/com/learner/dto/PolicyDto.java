package com.learner.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PolicyDto {
	
	private Long id;
    private String policyId;
    private String policyName;
    private String type;
    private String coverage;
    private String premium;
    private String description;

}
