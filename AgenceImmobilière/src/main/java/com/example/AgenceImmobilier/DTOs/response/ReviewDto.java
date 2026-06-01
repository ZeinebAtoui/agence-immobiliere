package com.example.AgenceImmobilier.DTOs.response;

import lombok.Data;

import java.util.Date;
@Data
public class ReviewDto {
    private long id;
    private String comment;
    private Date date;
    private int rating;

    private long logementId;
    private String logementTitle;
    private long userId;
    private String userName;
}
