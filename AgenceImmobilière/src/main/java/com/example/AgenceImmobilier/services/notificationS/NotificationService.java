package com.example.AgenceImmobilier.services.notificationS;

import com.example.AgenceImmobilier.DTOs.response.NotificationDTO;
import com.example.AgenceImmobilier.models.notification.Notification;
import com.example.AgenceImmobilier.models.user.UserModel;
import com.example.AgenceImmobilier.repositories.notificationR.NotificationRepository;
import com.example.AgenceImmobilier.services.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {
    @Autowired
     NotificationRepository notificationRepository;
    @Autowired
    private UserService userService;
    public void sendNotification(NotificationDTO notificationDTO){

        ZoneId zoneId = ZoneId.of("Africa/Tunis");
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        Notification notification= Notification.builder()
                .type(notificationDTO.getType())
                .content(notificationDTO.getContents())
                .fromUser(userService.findById(notificationDTO.getFromUserId()))
                .toUser(userService.findById(notificationDTO.getToUserId()))
                .read(false)
                .timeSent(localDateTime)
                .build();
        notificationRepository.save(notification);

    }
    public List<NotificationDTO> getAllNotificationsForUser(UserModel user) {
        List<Notification> notificationList= notificationRepository.findAllByToUser(user);

        return notificationList.stream()
                .map(notification -> NotificationDTO.builder()
                        .contents(notification.getContent())
                        .fromUserId(notification.getFromUser().getId())
                        .ToUserId(notification.getToUser().getId())
                        .type(notification.getType())
                        .timeSent(notification.getTimeSent())
                        .build())

                .collect(Collectors.toList());
    }
    public Notification markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(()->new EntityNotFoundException("Notification not found by id : "+notificationId));
        notification.setRead(true);

        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long id){
        Notification notification=notificationRepository.findById(id).get();
        try {
            notificationRepository.deleteById(id);
        }catch (EntityNotFoundException ex){
            throw new EntityNotFoundException("Notification not found by id : " +id);
        }
    }


}
