package com.example.AgenceImmobilier.models.logement;

import com.example.AgenceImmobilier.models.BaseEntity;
import com.example.AgenceImmobilier.models.booking.Booking;
import com.example.AgenceImmobilier.models.user.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "logement")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Logement  extends BaseEntity{

    private  String title;
    private  String description;
   /*-----Space------*/
    private int numOfBeds;
    private int numOfWc;
    private int numOfRooms;
    private boolean livingRoom;
    private double surface;
    /*-----Rules-----*/
    private boolean smoking=true;
    private boolean animals=false;
    private boolean parties;
    private int minRentDays=1;
    private int maxGuests=2;
    /*-----Location-----*/
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String address;
    private String postalCode;
    /*-----Cost---------*/
    private double minCost;
    private double costPerExtraGuest;
    /*-----Facilities-----*/
    private boolean wifi;
    private boolean ac;
    private boolean heating;
    private boolean kitchen;
    private boolean tv;
    private boolean parking;
    private boolean elevator;
    /*----Availability Days----*/
    private Date startDate;
    private Date endDate;
    /*--------Taype-------*/
    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private TypeLogement type;
    /*-------Reviews------*/
    private int numOfReviews;
    private double averageRating;

    /*--------Media---------*/
    @JsonManagedReference
    @OneToMany(mappedBy = "logement" ,fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Media> mediaList;

    /*-------Host------*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false)
    private UserModel host;
    /*------Booking-----*/
    @OneToMany(mappedBy = "logement", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Booking> bookings;
    /*------Reviews------*/
    @OneToMany(mappedBy = "logement", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> reviews;





}
