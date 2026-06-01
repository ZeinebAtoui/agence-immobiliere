package com.example.AgenceImmobilier.models.notification;

import com.example.AgenceImmobilier.models.BaseEntity;
import com.example.AgenceImmobilier.models.user.UserModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {

    private String type;
    @NotNull
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromUser_id",nullable = false)
    private UserModel fromUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toUser_id",nullable = false)
    private UserModel toUser;
   @Column(name = "is_read")
   private boolean read=false;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timeSent;

}
