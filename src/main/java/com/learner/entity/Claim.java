package com.learner.entity;

import java.sql.Blob;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String policyName;

    @Column(nullable = false)
    private String policyNo;

    @Column(nullable = false)
    private String policyId;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'InProgress'")
    private String status;

    @Lob
    @Column(nullable = false)
    private Blob image;

    private LocalDateTime updateDate;
}
