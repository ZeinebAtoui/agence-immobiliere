package com.example.AgenceImmobilier.repositories.logementR;

import com.example.AgenceImmobilier.models.logement.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    @Query("select r from review r where r.logement.host.id = ?1")
    List<Review> findAllByLogementHostId(Long id);
    List<Review> findAllByUserId(Long id);
}
