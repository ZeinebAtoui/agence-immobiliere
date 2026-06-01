package com.example.AgenceImmobilier.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRefreshResponse {
    private String accessToken ;

}
