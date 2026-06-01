package com.example.AgenceImmobilier.services.bookingS;

import com.example.AgenceImmobilier.DTOs.request.BookingPost;
import com.example.AgenceImmobilier.DTOs.response.BookingDto;
import com.example.AgenceImmobilier.converter.BookingConverter;
import com.example.AgenceImmobilier.exceptions.EntityNotFoundException;
import com.example.AgenceImmobilier.models.booking.Booking;
import com.example.AgenceImmobilier.repositories.bookingR.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImp implements BookingService{

    @Autowired
    private BookingRepository bookingRepository;


    @Override
    public BookingDto findById(Long id)  {
        Booking booking;
        booking=bookingRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Booking not found with id : "+ id));
        return BookingConverter.convertToDto(booking);
    }

    @Override
    public List<BookingDto> findAll() {
      return bookingRepository.findAll()
              .stream()
              .map(BookingConverter::convertToDto)
              .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> returnMyBookings(long id) {
        return bookingRepository.findAllByUserIdOrderByDateDesc(id)
                .stream()
                .map(BookingConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> returnLogementBookings(long id) {
        return bookingRepository.findAllByLogementIdOrderByDateDesc(id)
                .stream()
                .map(BookingConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> newBooking(BookingPost bookingPost) {
        List<BookingDto> bookingDtolList=new ArrayList<BookingDto>();
        BookingDto bookingDto=new BookingDto();
        BookingDto bookingDtoAfter=new BookingDto();
        bookingDto.setLogementId(bookingPost.getLogementId());
        bookingDto.setUserId(bookingPost.getUserId());

        Calendar cal=Calendar.getInstance();
        cal.setTime(bookingPost.getStartDate());

        Calendar cal2=Calendar.getInstance();
        cal2.setTime(bookingPost.getEndDate());

        while (cal.compareTo(cal2)<0){
            bookingDto.setDate(cal.getTime());

            bookingDtoAfter=save(bookingDto);
            bookingDtolList.add(bookingDtoAfter);
            cal.add(Calendar.DATE,1);
        }

        return bookingDtolList;
    }

    @Override
    public BookingDto save(BookingDto bookingDto) {
       Booking booking=BookingConverter.convert(bookingDto);
        booking=bookingRepository.save(booking);
        return BookingConverter.convertToDto(booking);
    }

    @Override
    public void deleteById(Long id) {

        bookingRepository.deleteById(id);
    }
}
