package com.example.AgenceImmobilier.models.user;


import com.example.AgenceImmobilier.models.BaseEntity;
import com.example.AgenceImmobilier.models.chat.ChatMessage;
import com.example.AgenceImmobilier.models.logement.Logement;
import com.example.AgenceImmobilier.models.logement.Review;
import com.example.AgenceImmobilier.models.notification.Notification;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.*;

@ToString(exclude = {"reviews"})
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserModel extends BaseEntity {
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private String providerId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    private String phone;
    private String gender;
    private String photoUrl;
    private String coverUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles=new HashSet<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    /*@OneToMany(mappedBy = "host" ,fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Logement> myLogements;*/

    /*-------Review-------*/

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Review> reviews;
    /*----------chat-----*/
    @OneToMany(mappedBy = "authorUser", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<ChatMessage> sentMessages = new ArrayList<>();
    @OneToMany(mappedBy = "recipientUser", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<ChatMessage> receivedMessages  = new ArrayList<>();
    /*-------notification--------*/
    @OneToMany(mappedBy = "fromUser", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Notification> sentNotification = new ArrayList<>();
    @OneToMany(mappedBy = "toUser", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Notification> receivedNotification  = new ArrayList<>();
    public UserModel(String firstname, String lastname, String username, String email, String password) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email=email;
        this.password = password;

    }

    public UserModel(String email, String password) {

        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", dob=" + dob +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", roles=" + roles +
                ", reviews=" + reviews +
                '}';
    }
}

