package com.project.bookommendbe.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@ToString(exclude = {"id", "userBook", "reviews"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String signUpId;
    private String username;

    @Column(unique = true)
    private String email;

    private String password;
    private String confirmPassword;

    private String fullName;
    private String  dateOfBirth;

    private String gender;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String phoneNumberTypical;

    private LocalDate createdAt;

    @OneToMany(mappedBy = "user")
    private List<UserBook> userBook;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    private int passwordAuthNumber;
}