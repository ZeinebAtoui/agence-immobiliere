package com.example.AgenceImmobilier.controllers;

import com.example.AgenceImmobilier.DTOs.response.BookingDto;
import com.example.AgenceImmobilier.DTOs.response.LogementDto;
import com.example.AgenceImmobilier.DTOs.response.ReviewDto;
import com.example.AgenceImmobilier.DTOs.response.UserDto;
import com.example.AgenceImmobilier.services.bookingS.BookingService;
import com.example.AgenceImmobilier.services.logementS.LogementService;
import com.example.AgenceImmobilier.services.logementS.ReviewService;
import com.example.AgenceImmobilier.services.user.UserService;
import com.example.AgenceImmobilier.utils.Helpers;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    LogementService logementService;
    @Autowired
    BookingService bookingService;
    @Autowired
    ReviewService reviewService;

    /*-------Users---------*/

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> returnAllUsers(){
        return ResponseEntity.ok().body(userService.findAllUser());
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<String> returnUserById(@PathVariable("id") Long id) throws JsonProcessingException{
        return ResponseEntity.ok().body(Helpers.convertToJson(userService.findUserDtoById(id)));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id){
        userService.deleteById(id);
        return ResponseEntity.ok().body("{\"Status\": \"Successful Deletion\"}");
    }
    /*-----------Logement-----------------------*/

    @GetMapping("/logement")
    public ResponseEntity<List<LogementDto>> returnAllLogements(){
        return ResponseEntity.ok().body(logementService.findAll());
    }

    /*------------Booking------------------------*/
    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDto>> returnAllBookings(){
        return ResponseEntity.ok().body(bookingService.findAll());
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<String> returnBookingById(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok().body(Helpers.convertToJson(bookingService.findById(id)));
    }

    @PutMapping("/bookings/{id}")
    public ResponseEntity<String> updateBooking(@PathVariable("id") Long id, @RequestBody   BookingDto bookingDto) throws JsonProcessingException {
        if(bookingDto!=null)
            return ResponseEntity.ok().body(Helpers.convertToJson(bookingService.save(bookingDto)));
        else
            return ResponseEntity.badRequest().body("{\"Status\": \"Booking not found\"}");
    }

    /*---------------Review----------------*/

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDto>> returnAllReviews(){
        return ResponseEntity.ok().body(reviewService.findAll());
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<String> returnReviewById(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok().body(Helpers.convertToJson(reviewService.findById(id)));
    }

    @PostMapping("/reviews")
    public ResponseEntity<String> createReview(@RequestBody ReviewDto reviewDto) throws JsonProcessingException {
        return ResponseEntity.ok().body(Helpers.convertToJson(reviewService.save(reviewDto)));
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<String> updateReview(@PathVariable("id") Long id, @RequestBody ReviewDto reviewDto) throws JsonProcessingException {
        if(reviewDto!=null)
            return ResponseEntity.ok().body(Helpers.convertToJson(reviewService.save(reviewDto)));
        else

            return ResponseEntity.badRequest().body("{\"Status\": \"Review not found\"}");
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReviewById(@PathVariable("id") Long id){
        reviewService.deleteById(id);
        return ResponseEntity.ok().body("{\"Status\": \"Successful Deletion\"}");
    }

}
