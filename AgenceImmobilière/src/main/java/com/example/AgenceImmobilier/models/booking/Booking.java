package com.example.AgenceImmobilier.models.booking;

import com.example.AgenceImmobilier.models.BaseEntity;
import com.example.AgenceImmobilier.models.logement.Logement;
import com.example.AgenceImmobilier.models.user.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "booking")
public class Booking extends BaseEntity {

    private Date date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="logement_id", nullable = false)
    private Logement logement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false)
    private UserModel user;
}
