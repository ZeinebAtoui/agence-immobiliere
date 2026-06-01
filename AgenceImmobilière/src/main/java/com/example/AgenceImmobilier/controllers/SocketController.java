package com.example.AgenceImmobilier.controllers;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.AgenceImmobilier.DTOs.response.ChatMessageDto;
import com.example.AgenceImmobilier.DTOs.response.NotificationDTO;
import com.example.AgenceImmobilier.DTOs.response.UserClient;
import com.example.AgenceImmobilier.DTOs.response.UserDto;
import com.example.AgenceImmobilier.config.MessageValidator;
import com.example.AgenceImmobilier.converter.UserConvert;
import com.example.AgenceImmobilier.services.chatS.ChatService;
import com.example.AgenceImmobilier.services.notificationS.NotificationService;
import com.example.AgenceImmobilier.services.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Component
@Log4j2
public class SocketController {

        @Value("${jwtSecret}")
        private String jwtSecret;
        @Autowired
        private SocketIOServer socketServer;
        @Autowired
        private UserService userService;

        @Autowired
        private ChatService chatService;
        @Autowired
        private NotificationService notificationService;
        @Autowired
        private MessageValidator messageValidator;
        public static Map<Long, UserClient> clientMap=new ConcurrentHashMap<>();

        SocketController(SocketIOServer socketServer){
                this.socketServer=socketServer;
                this.socketServer.addConnectListener(onUserConnectWithSocket);
                this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);
                this.socketServer.addEventListener("messageSendToUser", ChatMessageDto.class, onMessage);
        }

        public ConnectListener onUserConnectWithSocket=new ConnectListener() {
                @Override
                public void onConnect(SocketIOClient socketIOClient) {
                        String jwt=socketIOClient.getHandshakeData().getSingleUrlParam("Authorization");
                        if (jwt != null && jwt.length()>0 ){
                                String token=jwt.substring(7,jwt.length());
                                try {
                                        Claims claims = Jwts.parser()
                                                .setSigningKey(jwtSecret)
                                                .parseClaimsJws(token)
                                                .getBody();
                                        socketIOClient.getHandshakeData().getUrlParams().put("usrId", Collections.singletonList(claims.getId()));
                                        UserDto user= UserConvert.convertToDto(userService.findById(Long.valueOf(claims.getId())));
                                        log.info(user.getUsername()+" is connect /Perform operation on user connect in controller");
                                        clientMap.put(user.getId(),UserClient.builder().client(socketIOClient).user(user).build());
                                        List<UserDto> userConnected = clientMap.values().stream().map(clientMap-> { return clientMap.getUser();}).collect(Collectors.toList());
                                        socketIOClient.sendEvent("listconnected",userConnected);

                                }catch (SignatureException | MalformedJwtException | ExpiredJwtException |
                                        UnsupportedJwtException | IllegalArgumentException e) {
                                        socketIOClient.disconnect();
                                }
                        }else {
                                socketIOClient.disconnect();
                        }
                }
        };
        public DisconnectListener onUserDisconnectWithSocket = new DisconnectListener() {
                @Override
                public void onDisconnect(SocketIOClient client) {


                        try {
                                String jwt=client.getHandshakeData().getSingleUrlParam("Authorization");
                                if (jwt != null && jwt.length() > 0) {
                                        if(client.getHandshakeData().getUrlParams()==null) return;
                                        Long userId = Long.valueOf(client.getHandshakeData().getUrlParams().get("userId").get(0));
                                        UserClient userClient = clientMap.get(userId);
                                        if(userClient !=null){

                                                log.info(userClient.getUser().getUsername()+" is deconnect  in controller");
                                               // socketServer.getBroadcastOperations().sendEvent("userdeconnect",client,userClient.getUser());
                                                clientMap.remove(userId);
                                        }
                                }


                        }catch(Exception ex){
                                log.info("error in logout"+ex.getMessage());
                                client.disconnect();
                        }
                }
        };

        public DataListener<ChatMessageDto> onMessage= new DataListener<ChatMessageDto>() {
                @Override
                public void onData(SocketIOClient socketIOClient, ChatMessageDto messageDto, AckRequest ackRequest) throws Exception {
                        try {
                                Long userId = Long.valueOf(socketIOClient.getHandshakeData().getUrlParams().get("userId").get(0));
                                BindingResult bindingResult = new BeanPropertyBindingResult(messageDto, "message");
                                messageValidator.validate(messageDto, bindingResult);
                                if (bindingResult.hasErrors()) {
                                        ObjectMapper mapper = new ObjectMapper();
                                        socketIOClient.sendEvent("validation-error", mapper.writeValueAsString(bindingResult.getAllErrors()));
                                        return;
                                }

                                /*socketServer.getClient("fgerg").send()*/

                                chatService.submitMessage(messageDto);
                                ackRequest.sendAckData(messageDto);

                                        UserClient userconnect = clientMap.get(messageDto.getToUserId());
                                                String senderUsername=userService.findById(messageDto.getFromUserId()).getUsername();
                                                NotificationDTO notif=new NotificationDTO();
                                                notif.setContents(senderUsername + " sent you a new message!");
                                                notif.setFromUserId(userId);
                                                notif.setType("message");
                                                notif.setToUserId(messageDto.getToUserId());
                                                notificationService.sendNotification(notif);
                                                if(userconnect !=null && userconnect.getUser().getId() !=userId){

                                                        userconnect.getClient().sendEvent("chatmessage",messageDto);
                                                        userconnect.getClient().sendEvent("notificationMessage",notif);

                                                }





                        }catch (Exception ex) {
                                ackRequest.sendAckData("exception ::   ", ex.getMessage());
                                ex.printStackTrace();
                        }







                }
        };



}

