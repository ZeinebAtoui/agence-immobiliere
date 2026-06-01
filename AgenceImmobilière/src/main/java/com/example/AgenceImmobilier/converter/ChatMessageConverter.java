package com.example.AgenceImmobilier.converter;

import com.example.AgenceImmobilier.DTOs.response.ChatMessageDto;
import com.example.AgenceImmobilier.models.chat.ChatMessage;
import com.example.AgenceImmobilier.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageConverter {
    @Autowired
    private UserService userService;
    private static UserService userServiceStatic;

    @Autowired
    public void setStatic(){
        this.userServiceStatic=userService;
    }

    public static ChatMessageDto convertToDto(ChatMessage chatMessage){
        ChatMessageDto chatMessageDto=new ChatMessageDto();
        chatMessageDto.setFromUserId(chatMessage.getAuthorUser().getId());
        chatMessageDto.setToUserId(chatMessage.getRecipientUser().getId());
        chatMessageDto.setContents(chatMessage.getContents());

        return chatMessageDto;
    }

    public static ChatMessage convert(ChatMessageDto chatMessageDto){
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setContents(chatMessageDto.getContents());
        chatMessage.setAuthorUser(userServiceStatic.findById(chatMessageDto.getFromUserId()));
        chatMessage.setRecipientUser(userServiceStatic.findById(chatMessageDto.getToUserId()));

        return chatMessage;
    }

}
