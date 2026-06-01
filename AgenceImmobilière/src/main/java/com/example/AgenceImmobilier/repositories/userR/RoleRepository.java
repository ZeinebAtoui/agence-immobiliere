package com.example.AgenceImmobilier.repositories.userR;

import com.example.AgenceImmobilier.models.user.ERole;
import com.example.AgenceImmobilier.models.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    @Query("select r from Role r where r.name = ?1")
    Optional<Role> findRoleByName(ERole name);
}

