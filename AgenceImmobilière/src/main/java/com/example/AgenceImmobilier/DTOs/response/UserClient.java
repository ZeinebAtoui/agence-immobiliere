package com.example.AgenceImmobilier.DTOs.response;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserClient {
    private UserDto user ;
    private SocketIOClient client ;
}
