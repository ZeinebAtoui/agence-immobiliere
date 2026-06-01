package com.example.AgenceImmobilier.repositories.notificationR;

import com.example.AgenceImmobilier.models.notification.Notification;
import com.example.AgenceImmobilier.models.user.UserModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Transactional
@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    @Query("select n from Notification n where n.toUser = :user")
    List<Notification> findAllByToUser(@Param("user") UserModel user);
}
