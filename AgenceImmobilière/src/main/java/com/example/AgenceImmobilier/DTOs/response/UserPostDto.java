package com.example.AgenceImmobilier.DTOs.response;


import lombok.Data;

import java.util.Date;

@Data
public class UserPostDto {

    private String username;

    private String firstname;
    private String lastname;
    private String email;
    private String number;
    private String gender;
    private Date dob;
}
