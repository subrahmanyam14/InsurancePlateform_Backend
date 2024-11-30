package com.learner.entity;

import java.sql.Blob;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Entity
@Data
public class Payment {
    


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String policyId;
    private String policyNo;
    private String policyName;
    private String amount;
    private String referenceId;
    private String transactionId;
    private String email;
    private String date;
    @Lob
    private Blob imageProof;
}