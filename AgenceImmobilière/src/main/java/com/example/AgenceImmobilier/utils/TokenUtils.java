package com.example.AgenceImmobilier.utils;

import com.example.AgenceImmobilier.security.services.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TokenUtils {
    public TokenUtils() {
    }

    public Long ExtractId(){
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetailsImpl.getId();
    }
    public UserDetailsImpl extractUser(){
        return  (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
    }
}
