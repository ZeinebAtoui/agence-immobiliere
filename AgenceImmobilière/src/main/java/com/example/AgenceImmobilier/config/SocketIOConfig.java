package com.example.AgenceImmobilier.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.AgenceImmobilier.security.jwt.JwtUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collections;

@CrossOrigin
@Component
@Log4j2
public class SocketIOConfig {
    @Value("${jwtSecret}")
    private String jwtSecret;
    @Value("${socket.host}")
    private String SOCKETHOST;
    @Value("${socket.port}")
    private int SOCKETPORT;
    private SocketIOServer server;
    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public SocketIOServer socketIOServer(){
        Configuration config=new Configuration();
        config.setHostname(SOCKETHOST);
        config.setPort(SOCKETPORT);

        server =new SocketIOServer(config);

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                //String jwt ="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2Iiwic3ViIjoiYWxpYWEiLCJpYXQiOjE2OTYxNzc0MjMsImV4cCI6MTY5NjI2MzgyM30.IWruS5pf3qSrFriZIzqsTR6rx_3V5GcL76cOiF7x_j8";
                String jwt =socketIOClient.getHandshakeData().getSingleUrlParam("Authorization");
                if (jwt != null && jwt.length() > 0) {
                    String token = jwt.substring(7,jwt.length());
                    try {
                        Claims claims = Jwts.parser()
                                .setSigningKey(jwtSecret)
                                .parseClaimsJws(token)
                                .getBody();
                        socketIOClient.getHandshakeData().getUrlParams().put("userId", Collections.singletonList(claims.getId()));
                    } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
                        log.error("Error occurred while processing JWT token: " + e.getMessage());
                        socketIOClient.disconnect();
                    }
                } else {
                    socketIOClient.disconnect();
                }
            }
        });
        server.start();

        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                socketIOClient.getNamespace().getAllClients().stream().forEach(data->{
                    log.info("user disconnected " + data.getSessionId().toString());
                });

            }
        });
        return server;
    }

    @PreDestroy
    public void stopSocketIOServer() {
        this.server.stop();
    }


}
