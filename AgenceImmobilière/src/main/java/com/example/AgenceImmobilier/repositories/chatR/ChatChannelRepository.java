package com.example.AgenceImmobilier.repositories.chatR;

import com.example.AgenceImmobilier.models.chat.ChatChannel;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Transactional
@Repository
public interface ChatChannelRepository extends JpaRepository<ChatChannel,String> {
    @Query("SELECT c FROM ChatChannel c " +
            "WHERE (c.userOne.id = :userOneId AND c.userTwo.id = :userTwoId) " +
            "   OR (c.userOne.id = :userTwoId AND c.userTwo.id = :userOneId)")
    public List<ChatChannel> findExistingChannel(
            @Param("userOneId") Long userOneId, @Param("userTwoId") Long userTwoId);

    @Query(" SELECT"
            + "    id"
            + "  FROM"
            + "    ChatChannel c"
            + "  WHERE"
            + "    c.userOne.id IN (:userIdOne, :userIdTwo)"
            + "  AND"
            + "    c.userTwo.id IN (:userIdOne, :userIdTwo)")
    public String getChannelId(
            @Param("userIdOne") Long userIdOne, @Param("userIdTwo") Long userIdTwo);

    @Query("SELECT c FROM ChatChannel c WHERE c.id = :id")
    public ChatChannel getChannelDetails(@Param("id") Long id);
}
