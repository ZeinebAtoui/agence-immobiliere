package com.example.AgenceImmobilier.DTOs.response;

import lombok.Data;

import java.util.Date;

@Data
public class BookingDto {
    private long id;
    private Date date;

    private long logementId;
    private String logementTitle;
    private long userId;
}
