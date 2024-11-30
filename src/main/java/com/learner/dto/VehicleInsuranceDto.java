package com.learner.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VehicleInsuranceDto {
	
	private Long id;
    private String email;
    private String type;
    private String policyId;
    private String policyNo;
    private String policyName;
    private String vehicleNumber;
    private String vehicleCompany;
    private String vehicleModel;
    private String chassisNumber;
    private String manufacturingYear;
    private String documentimage;
    private String status;

}
