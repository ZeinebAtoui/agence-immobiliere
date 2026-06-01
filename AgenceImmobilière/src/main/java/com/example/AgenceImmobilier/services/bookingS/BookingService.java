package com.example.AgenceImmobilier.services.bookingS;

import com.example.AgenceImmobilier.DTOs.request.BookingPost;
import com.example.AgenceImmobilier.DTOs.response.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto findById(Long id) ;
    List<BookingDto> findAll();
    List<BookingDto> returnMyBookings(long id);
    List<BookingDto> returnLogementBookings(long id);

    List<BookingDto> newBooking(BookingPost bookingPost);
    BookingDto save(BookingDto bookingDto);

    void deleteById(Long id);
}
