package com.learner.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TravelInsuranceDTO {
	
	private Long id;
    private String email;
    private String type;
    private String policyNo;
    private String policyId;
    private String policyName;
    private String destination;
    private String organization;
    private String startTime;
    private String endTime;
    private String modeOfTravel;
    private String ticketId;
    private String documentimage;
    private String nomineeName;
    private String nomineeRelation;
    private String nomineeAge;
    private String nomineeAadharNo;
    private String status;

}
