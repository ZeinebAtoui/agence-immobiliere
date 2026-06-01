package com.example.AgenceImmobilier.models.chat;

import com.example.AgenceImmobilier.models.BaseEntity;
import com.example.AgenceImmobilier.models.user.UserModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chatMessage")
public class ChatMessage extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorUserId",nullable = false)
    private UserModel authorUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipientUserId",nullable = false)
    private UserModel recipientUser;
    @NotNull
    private String contents;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timeSent;
}
