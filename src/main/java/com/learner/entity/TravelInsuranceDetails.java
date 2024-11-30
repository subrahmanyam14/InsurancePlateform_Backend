package com.learner.entity;

import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="travelinsurance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelInsuranceDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String type = PolicyType.TRAVEL.toString();
    private String policyNo;
    private String policyId;
    private String policyName;
    private String destination;
    private String organization;
    private String startTime;
    private String endTime;
    private String modeOfTravel;
    private String ticketId;
    @Lob
    private Blob documentimage;
    private String nomineeName;
    private String nomineeRelation;
    private String nomineeAge;
    private String nomineeAadharNo;
    private String status;
}