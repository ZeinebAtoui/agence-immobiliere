package com.example.AgenceImmobilier.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatChannelInitializationDto {
    private Long userIdOne;
    private Long userIdTwo;
}
