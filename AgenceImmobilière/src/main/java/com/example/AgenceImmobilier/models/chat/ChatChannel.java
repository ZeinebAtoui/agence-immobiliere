package com.example.AgenceImmobilier.models.chat;

import com.example.AgenceImmobilier.models.user.UserModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "chatChannel")
public class ChatChannel  {
    @Id
    @NotNull
    private String uuid;
    @OneToOne
    @JoinColumn(name = "userIdOne")
    private UserModel userOne;

    @OneToOne
    @JoinColumn(name = "userIdTwo")
    private UserModel userTwo;

    public ChatChannel(UserModel userOne, UserModel userTwo) {
        this.uuid = UUID.randomUUID().toString();
        this.userOne = userOne;
        this.userTwo = userTwo;
    }
}
