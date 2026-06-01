package com.example.AgenceImmobilier.models.logement;

import com.example.AgenceImmobilier.models.BaseEntity;
import com.example.AgenceImmobilier.models.user.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "review")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Review extends BaseEntity {

    private String comment;
    @CreationTimestamp
    private Date date;

    private int rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="logement_id", nullable = false)
    private Logement logement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false)
    private UserModel user;
}
