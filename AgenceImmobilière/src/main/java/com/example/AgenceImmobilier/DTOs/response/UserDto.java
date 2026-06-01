package com.example.AgenceImmobilier.DTOs.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDto {

    private long id;
    @NotNull
    @NotEmpty
    private String username;

    private String firstName;
    private String lastName;

    private String email;
    private String number;
    private String gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    private String photoUrl;
    private String coverUrl;
}
