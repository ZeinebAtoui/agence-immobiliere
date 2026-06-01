package com.example.AgenceImmobilier.DTOs.response;

import com.example.AgenceImmobilier.models.logement.Media;
import com.example.AgenceImmobilier.models.logement.TypeLogement;
import com.example.AgenceImmobilier.models.user.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LogementDto {
    private Long id;
    private String title;

    private TypeLogement type;
    private int numOfBeds;
    private int numOfWc;
    private int numOfRooms;
    private boolean livingRoom;
    private double squareFootage;

    private String description;

    private boolean smoking;
    private boolean animals;
    private boolean parties;
    private int minRentDays;
    private int maxGuests;

    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String neighborhood;
    private String address;
    private String postalCode;
    private String transportation;

    private double minCost;
    private double costPerExtraGuest;

    private boolean wifi;
    private boolean ac;
    private boolean heating;
    private boolean kitchen;
    private boolean tv;
    private boolean parking;
    private boolean elevator;

    private Date startDate;
    private Date endDate;

    private int numOfReviews;
    private double averageRating;
    private UserDto host;
    private List<BookingDto> Bookings;
    private List<Media> mediaList;
    private List<ReviewDto> reviews;
}
