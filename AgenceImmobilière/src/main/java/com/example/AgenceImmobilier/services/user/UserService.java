package com.example.AgenceImmobilier.services.user;


import com.example.AgenceImmobilier.DTOs.response.UserDto;
import com.example.AgenceImmobilier.converter.UserConvert;
import com.example.AgenceImmobilier.exceptions.EntityNotFoundException;
import com.example.AgenceImmobilier.models.user.UserModel;
import com.example.AgenceImmobilier.repositories.userR.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserModel saveUser(UserModel user){
        return userRepository.save(user);
    }
    public Boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }
    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public UserModel findById(Long id){
        UserModel user=userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("User not found with id : "+id));
        return user;
    }
    public UserModel findByUsername(String username){
        UserModel user=userRepository.findByUsernameOrEmail(username)
                .orElseThrow(()->new EntityNotFoundException("User not found By username : "+username  ));
        return user;
    }
    public List<UserDto> findAllUser(){
        return userRepository.findAll()
                .stream()
                .map(UserConvert::convertToDto)
                .collect(Collectors.toList());
    }
    public Boolean existsByUsernameOrEmail(String usernameOrEmail){
        return userRepository.existsByUsernameOrEmail(usernameOrEmail);
    }
   public UserDto findUserDtoById(Long id ){
       UserModel user=userRepository.findById(id)
               .orElseThrow(()-> new EntityNotFoundException("User not found with id : "+id));
       return UserConvert.convertToDto(user);
   }

    public void deleteById(Long id) {
        try{
            userRepository.deleteById(id);
        }catch (Exception ex){
            throw new EntityNotFoundException("user not found with id : " + id);
        }
    }




}

