package com.example.AgenceImmobilier.DTOs.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDto {
    @NotBlank(message = "refresh token is required")
    private String refreshToken ;

}
