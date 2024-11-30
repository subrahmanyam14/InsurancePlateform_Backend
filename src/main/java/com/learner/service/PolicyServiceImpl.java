package com.learner.service;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learner.dto.PolicyDto;
import com.learner.entity.Policies;
import com.learner.entity.PolicyType;
import com.learner.repository.PolicyRepository;


@Service
public class PolicyServiceImpl {
	
	

    @Autowired
    private PolicyRepository policyrepository;

   
    private PolicyDto toDto(Policies policies)
    {
    	PolicyDto dto = new PolicyDto();
    	dto.setId(policies.getId());
    	dto.setPolicyId(policies.getPolicyId());
    	dto.setDescription(policies.getDescription());
    	dto.setCoverage(policies.getCoverage());
    	dto.setPolicyName(policies.getPolicyName());
    	dto.setPremium(policies.getPremium());
    	dto.setType(policies.getType());
    	dto.setPolicyName(policies.getPolicyName());
    	return dto;
    }
    
    private Policies toEntity(PolicyDto dto)
    {
    	Policies pol = new Policies();
    	pol.setCoverage(dto.getCoverage());
    	pol.setDescription(dto.getDescription());
    	pol.setPolicyId(dto.getPolicyId());
    	pol.setPolicyName(dto.getPolicyName());
    	pol.setPremium(dto.getPremium());
    	pol.setType(dto.getType());
    	pol.setPolicyName(dto.getPolicyName());
    	return pol;
    }
    
    public String savePolicy(PolicyDto dto) {
    	if(dto.getType().equalsIgnoreCase(PolicyType.HEALTH.toString()))
    	{
    		dto.setType(PolicyType.HEALTH.toString());
    	}
    	else if(dto.getType().equalsIgnoreCase(PolicyType.HOME.toString()))
    	{
    		dto.setType(PolicyType.HOME.toString());
    	}
    	else if(dto.getType().equalsIgnoreCase(PolicyType.LIFE.toString()))
    	{
    		dto.setType(PolicyType.LIFE.toString());
    	}
    	else if(dto.getType().equalsIgnoreCase(PolicyType.TRAVEL.toString()))
    	{
    		dto.setType(PolicyType.TRAVEL.toString());
    	}
    	else if(dto.getType().equalsIgnoreCase(PolicyType.VEHICLE.toString()))
    	{
    		dto.setType(PolicyType.VEHICLE.toString());
    	}

    	Policies policy = toEntity(dto);
    	policy.setPolicyId(getPolicyId());
        policyrepository.save(policy);
        return "policy saved sucessfully";
        
    }

    
    public List<PolicyDto> getAllPolicies()
    {
        List<Policies> policies=policyrepository.findAll();
        List<PolicyDto> dtos = new ArrayList<PolicyDto>();
        for(Policies i : policies)
        {
        	dtos.add(toDto(i));
        }
        return dtos;
    }

    
    public String updatePolicy(PolicyDto dto)
    {
    	Policies policy = toEntity(dto);
    	policy.setId(dto.getId());
        policyrepository.save(policy);
        return "policy updated sucessfully";
    }

    
    public String deletePolicy(Long id)
    {
        policyrepository.deleteById(id);
        return "policy deleted sucessfully";
    }
    
    public String getPolicyId()
    {
        Random random =new Random();
        int randomNumber=random.nextInt(99999999);
        String id=Integer.toString(randomNumber);
        while(id.length()<8)
        {
            id="0"+id;
                
        }
        return id;
    }


        
    }