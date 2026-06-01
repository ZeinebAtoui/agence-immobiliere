package com.example.AgenceImmobilier.DTOs.request;

import lombok.Data;

import java.util.Date;

@Data
public class BookingPost {
    private Date startDate;
    private Date endDate;
    private long logementId;
    private long userId;
}
