package com.example.AgenceImmobilier.repositories.userR;

import com.example.AgenceImmobilier.models.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel,Long> {

    @Query("select (count(u) > 0) from UserModel u where u.username = ?1")
    Boolean existsByUsername(String username);

    @Query("select (count(u) > 0) from UserModel u where u.email = ?1")
    Boolean existsByEmail(String email);

    @Query("select u from UserModel u where u.enabled = true")
    List<UserModel> findAllByEnabledTrue();

    @Query("select (count(u) > 0) from UserModel u where u.username = ?1 or u.email = ?1")
    Boolean existsByUsernameOrEmail(String username);

    @Query("select u from UserModel u where u.username = ?1 or u.email = ?1")
    Optional<UserModel> findByUsernameOrEmail(String username);

    @Query("select u from UserModel u where u.email = ?1")
    Optional<UserModel> findByEmail(String email);
}
