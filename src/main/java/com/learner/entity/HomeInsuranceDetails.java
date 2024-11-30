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
@Table(name="home")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeInsuranceDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String type = PolicyType.HOME.toString();
    private String policyNo;
    private String policyId;
    private String policyName;
    private String houseno;
    private String owner;
    private String location;
    @Lob
    private Blob documentimage;
    private String status;
}