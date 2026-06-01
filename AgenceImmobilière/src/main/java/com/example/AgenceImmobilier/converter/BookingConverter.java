package com.example.AgenceImmobilier.converter;

import com.example.AgenceImmobilier.DTOs.response.BookingDto;
import com.example.AgenceImmobilier.models.booking.Booking;
import com.example.AgenceImmobilier.services.logementS.LogementService;
import com.example.AgenceImmobilier.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingConverter {
    @Autowired
    private LogementService logementService;
    private static LogementService logementServiceStatic;
    @Autowired
    private UserService userService;
    private static UserService userServiceStatic;

    @Autowired
    public void setStatic(){
        this.logementServiceStatic=logementService;
        this.userServiceStatic=userService;
    }
    public static BookingDto convertToDto(Booking booking){
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setDate(booking.getDate());

        bookingDto.setLogementId(booking.getLogement().getId());
        bookingDto.setLogementTitle(booking.getLogement().getTitle());
        bookingDto.setUserId(booking.getUser().getId());

        return bookingDto;
    }
    public static Booking convert(BookingDto bookingDto){
        Booking booking = new Booking();

        booking.setId(bookingDto.getId());
        booking.setDate(bookingDto.getDate());

        booking.setLogement(logementServiceStatic.findById(bookingDto.getLogementId()));
        booking.setUser(userServiceStatic.findById(bookingDto.getUserId()));

        return booking;
    }
}
