package com.example.AgenceImmobilier.repositories.logementR;

import com.example.AgenceImmobilier.models.logement.Logement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface LogementRepository extends JpaRepository<Logement,Long>, JpaSpecificationExecutor<Logement> {
    List<Logement> findAllByHostId(Long id);
    @Query("""
            select l from Logement l
            where l.country = ?1 and l.city = ?2 and l.startDate < ?3 and l.endDate > ?4 and l.maxGuests >= ?5
            order by l.minCost""")
    List<Logement> findAllByCountryAndCityAndStartDateBeforeAndEndDateAfterAndMaxGuestsIsGreaterThanEqualOrderByMinCost(String country, String city, Date startDate, Date endDate, int guests);
}
