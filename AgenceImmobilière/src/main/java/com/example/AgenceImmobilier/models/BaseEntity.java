package com.example.AgenceImmobilier.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor

public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private Date updatedAt;
    private Boolean enabled=false;

}
