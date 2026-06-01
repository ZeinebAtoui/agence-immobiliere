package com.example.AgenceImmobilier.config;

import com.example.AgenceImmobilier.DTOs.response.ChatMessageDto;
import com.example.AgenceImmobilier.models.chat.ChatMessage;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MessageValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ChatMessageDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChatMessageDto message = (ChatMessageDto) target;
        if (isNullOrBlank(message.getContents())) {
            errors.rejectValue("content", "Message text cannot be longer than 140 characters.");
        }


    }

    public static boolean isNullOrBlank(String param) {

        if(param.isEmpty()) return true;
        return false;

    }
}
