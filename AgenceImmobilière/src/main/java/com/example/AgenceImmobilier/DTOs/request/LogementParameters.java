package com.example.AgenceImmobilier.DTOs.request;

import com.example.AgenceImmobilier.models.logement.TypeLogement;
import lombok.Data;

import java.util.Date;
@Data
public class LogementParameters {
    private Date startDate;
    private Date endDate;
    private int guests;
    private String country;
    private String city;

    private TypeLogement type;
    private Double maxCost;
    private Boolean wifi;
    private Boolean ac;
    private Boolean heating;
    private Boolean kitchen;
    private Boolean tv;
    private Boolean parking;
    private Boolean elevator;
}
