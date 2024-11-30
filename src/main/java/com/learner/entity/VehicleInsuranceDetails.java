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
@Table(name="vehicleinsurance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleInsuranceDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String type = PolicyType.VEHICLE.toString();
    private String policyId;
    private String policyNo;
    private String policyName;
    private String vehicleNumber;
    private String vehicleCompany;
    private String vehicleModel;
    private String chassisNumber;
    private String manufacturingYear;
    @Lob
    private Blob documentimage;
    private String status;
}
