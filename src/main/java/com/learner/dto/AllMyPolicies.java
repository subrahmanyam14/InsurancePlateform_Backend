package com.learner.dto;

import lombok.Data;

@Data
public class AllMyPolicies {
	
	private HealthInsuranceDTO healthInsuranceDTO;
	private HomeInsuranceDTO homeInsuranceDTO;
	private LifeInsuranceDTO lifeInsuranceDTO;
	private TravelInsuranceDTO travelInsuranceDTO;
	private VehicleInsuranceDto vehicleInsuranceDto;

}
