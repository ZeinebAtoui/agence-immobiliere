package com.example.AgenceImmobilier.services.user;

import com.example.AgenceImmobilier.exceptions.EntityNotFoundException;
import com.example.AgenceImmobilier.models.user.ERole;
import com.example.AgenceImmobilier.models.user.Role;
import com.example.AgenceImmobilier.repositories.userR.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findRoleByName(ERole name){
        Role role=roleRepository.findRoleByName(name)
                .orElseThrow(()->new EntityNotFoundException("role not found with name :"+name));
        return role;
    }
}
