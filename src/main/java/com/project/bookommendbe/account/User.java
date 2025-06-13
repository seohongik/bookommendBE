package com.project.bookommendbe.account;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@EqualsAndHashCode
@ToString
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String signUpId;
    private String username;
    private String email;
    private String password;

    private String fullName;
    private LocalDate dateOfBirth;

    private LocalDate createdAt;

    @ManyToOne
    private Goals goals;
}