package com.example.AgenceImmobilier.repositories.chatR;

import com.example.AgenceImmobilier.models.chat.ChatMessage;
import com.example.AgenceImmobilier.models.user.UserModel;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Transactional
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    @Query("SELECT m FROM ChatMessage m " +
            "WHERE (m.authorUser.id = :userIdOne AND m.recipientUser.id = :userIdTwo) " +
            "   OR (m.authorUser.id = :userIdTwo AND m.recipientUser.id = :userIdOne) " +
            "ORDER BY m.createdAt DESC")
    public List<ChatMessage> getExistingChatMessages(
            @Param("userIdOne") Long userIdOne, @Param("userIdTwo") Long userIdTwo, Pageable pageable);

    @Query("select (count(c) > 0) from ChatMessage c where c.authorUser = :userIdOne and c.recipientUser = :userIdTwo")
    boolean existsByAuthorUserAndRecipientUser(@Param("userIdOne") UserModel userIdOne, @Param("userIdTwo") UserModel userIdTwo);
}
