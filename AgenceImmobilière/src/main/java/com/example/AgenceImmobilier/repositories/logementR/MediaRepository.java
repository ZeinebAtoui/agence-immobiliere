package com.example.AgenceImmobilier.repositories.logementR;

import com.example.AgenceImmobilier.models.logement.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media,Long> {
}
