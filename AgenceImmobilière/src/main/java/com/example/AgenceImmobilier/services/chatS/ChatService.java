package com.example.AgenceImmobilier.services.chatS;

import com.example.AgenceImmobilier.DTOs.response.ChatChannelInitializationDto;
import com.example.AgenceImmobilier.DTOs.response.ChatMessageDto;
import com.example.AgenceImmobilier.converter.ChatMessageConverter;
import com.example.AgenceImmobilier.exceptions.EntityNotFoundException;
import com.example.AgenceImmobilier.exceptions.IsSameUserException;
import com.example.AgenceImmobilier.models.chat.ChatChannel;
import com.example.AgenceImmobilier.models.chat.ChatMessage;
import com.example.AgenceImmobilier.repositories.chatR.ChatChannelRepository;
import com.example.AgenceImmobilier.repositories.chatR.ChatMessageRepository;
import com.example.AgenceImmobilier.services.user.UserService;
import com.google.common.collect.Lists;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService {
    @Autowired
    private ChatChannelRepository chatChannelRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserService userService;

    private final int MAX_PAGABLE_CHAT_MESSAGES = 100;

    private String getExistingChannel(ChatChannelInitializationDto chatChannelInitializationDTO) {
        List<ChatChannel> channel = chatChannelRepository
                .findExistingChannel(
                        chatChannelInitializationDTO.getUserIdOne(),
                        chatChannelInitializationDTO.getUserIdTwo()
                );

        return (channel != null && !channel.isEmpty()) ? channel.get(0).getUuid() : null;
    }

    private String newChatSession(ChatChannelInitializationDto chatChannelInitializationDTO)
            throws BeansException, EntityNotFoundException {
        ChatChannel channel = new ChatChannel(
                userService.findById(chatChannelInitializationDTO.getUserIdOne()),
                userService.findById(chatChannelInitializationDTO.getUserIdTwo())
        );

        chatChannelRepository.save(channel);

        return channel.getUuid();
    }

    // verifie sender # recipien si difrnt retune id chanel ou create nexw chanel
    public String establishChatSession(ChatChannelInitializationDto chatChannelInitializationDTO)
            throws IsSameUserException, BeansException, EntityNotFoundException {
        if (chatChannelInitializationDTO.getUserIdOne() == chatChannelInitializationDTO.getUserIdTwo()) {
            throw new IsSameUserException();
        }

        String uuid = getExistingChannel(chatChannelInitializationDTO);

        // If channel doesn't already exist, create a new one
        return (uuid != null) ? uuid : newChatSession(chatChannelInitializationDTO);
    }

    public void submitMessage(ChatMessageDto chatMessageDTO)
            throws BeansException, EntityNotFoundException, IsSameUserException {
        ChatChannelInitializationDto channel=new ChatChannelInitializationDto(chatMessageDTO.getFromUserId(),chatMessageDTO.getToUserId());

            String idChannel= establishChatSession(channel);
        ZoneId zoneId = ZoneId.of("Africa/Tunis");

        LocalDateTime localDateTime = LocalDateTime.now(zoneId);

           try{

               ChatMessage chatMessage = ChatMessageConverter.convert(chatMessageDTO);

               chatMessage.setTimeSent(localDateTime);
               chatMessageRepository.save(chatMessage);

            }catch (Exception ex){
               ex.printStackTrace();
           }

    }

    public List<ChatMessageDto> getExistingChatMessages(Long channelId) {
        ChatChannel channel = chatChannelRepository.getChannelDetails(channelId);

        List<ChatMessage> chatMessages =
                chatMessageRepository.getExistingChatMessages(
                        channel.getUserOne().getId(),
                        channel.getUserTwo().getId(),
                        PageRequest.of(0, MAX_PAGABLE_CHAT_MESSAGES)
                );

        // TODO: fix this
        List<ChatMessage> messagesByLatest = Lists.reverse(chatMessages);

        return messagesByLatest
                .stream()
                .map(ChatMessageConverter::convertToDto)
                .collect(Collectors.toList());
    }


}
