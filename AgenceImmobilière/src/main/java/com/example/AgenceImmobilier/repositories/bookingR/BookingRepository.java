package com.example.AgenceImmobilier.repositories.bookingR;

import com.example.AgenceImmobilier.models.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findAllByLogementIdAndDateAfterAndAndDateBefore(long id, Date date1, Date date2);
    List<Booking> findAllByUserIdOrderByDateDesc(long id);
    List<Booking> findAllByLogementIdOrderByDateDesc(long id);
}
