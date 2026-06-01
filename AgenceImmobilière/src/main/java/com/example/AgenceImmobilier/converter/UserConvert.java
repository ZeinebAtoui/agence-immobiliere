package com.example.AgenceImmobilier.converter;

import com.example.AgenceImmobilier.DTOs.response.UserDto;
import com.example.AgenceImmobilier.models.user.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserConvert {
    public static UserDto convertToDto (UserModel userModel){
        UserDto userDto=new UserDto();
        userDto.setId(userModel.getId());
        userDto.setUsername(userModel.getUsername());
        userDto.setFirstName(userModel.getFirstname());
        userDto.setLastName(userModel.getLastname());
        userDto.setEmail(userModel.getEmail());
        userDto.setNumber(userModel.getPhone());
        userDto.setGender(userModel.getGender());
        userDto.setDob(userModel.getDob());
        userDto.setPhotoUrl(userModel.getPhotoUrl());
        userDto.setCoverUrl(userModel.getCoverUrl());
        return  userDto;
    }
    public static UserModel convert (UserDto userDto){
        UserModel userModel=new UserModel();

        userModel.setId(userDto.getId());
        userModel.setUsername(userDto.getUsername());
        userModel.setFirstname(userDto.getFirstName());
        userModel.setEmail(userDto.getEmail());
        userModel.setPhone(userDto.getNumber());
        userModel.setGender(userDto.getGender());
        userModel.setDob(userDto.getDob());
        userModel.setPhotoUrl(userDto.getPhotoUrl());
        userModel.setCoverUrl(userDto.getCoverUrl());
        return userModel;
    }
}
