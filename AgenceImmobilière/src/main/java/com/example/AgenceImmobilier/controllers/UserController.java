package com.example.AgenceImmobilier.controllers;

import com.example.AgenceImmobilier.DTOs.request.BookingPost;
import com.example.AgenceImmobilier.DTOs.request.LogementParameters;
import com.example.AgenceImmobilier.DTOs.response.BookingDto;
import com.example.AgenceImmobilier.DTOs.response.LogementDto;
import com.example.AgenceImmobilier.DTOs.response.ReviewDto;
import com.example.AgenceImmobilier.DTOs.response.UserPostDto;
import com.example.AgenceImmobilier.converter.UserConvert;
import com.example.AgenceImmobilier.models.user.UserModel;
import com.example.AgenceImmobilier.services.bookingS.BookingService;
import com.example.AgenceImmobilier.services.flicker.FlickrService;
import com.example.AgenceImmobilier.services.logementS.LogementService;
import com.example.AgenceImmobilier.services.logementS.ReviewService;
import com.example.AgenceImmobilier.services.user.UserService;
import com.example.AgenceImmobilier.utils.Helpers;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user/")
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
     LogementService logementService;
    @Autowired
    FlickrService flickrService;
    @Autowired
    BookingService bookingService;
    @Autowired
    ReviewService reviewService;
    /*-----------logement----------*/

    @GetMapping("/logement/{id}")
    public ResponseEntity<String> returnLogementById(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok().body(Helpers.convertToJson(logementService.findDtoById(id)));
    }

    @GetMapping("/search")
    public Page<LogementDto> searchLogements(@ModelAttribute LogementParameters parameters,
                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return logementService.searchLogements(parameters, page, pageSize);
    }
    /*-----------Booking---------*/

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDto>> returnMyActiveBookings(Principal principal){
        UserModel user = userService.findByUsername(principal.getName());
        return ResponseEntity.ok().body(bookingService.returnMyBookings(user.getId()));
    }

    @PostMapping("/bookings")
    public ResponseEntity<String> createBooking(@RequestBody BookingPost bookingPost, Principal principal) throws JsonProcessingException {
        UserModel user = userService.findByUsername(principal.getName());
        bookingPost.setUserId(user.getId());

        return ResponseEntity.ok().body(Helpers.convertToJson(bookingService.newBooking(bookingPost)));
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<String> deleteBookingById(@PathVariable("id") Long id,Principal principal){
        BookingDto bookingDto=bookingService.findById(id);
        try{
            UserModel user = userService.findByUsername(principal.getName());
            if(user.getId() ==bookingDto.getUserId() ){
                bookingService.deleteById(id);
                return ResponseEntity.ok().body("{\"Status\": \"Successful Deletion\"}");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Status\": \"User has not booked this accommodation\"}");

            }

        }catch (Exception ex){
            return ResponseEntity.badRequest().body("{\"Status\": \"Booking does not exist\"}");
        }

    }
    /*---------users-------------*/
    @GetMapping("/profile")
    public ResponseEntity<String> returnProfile(Principal principal) throws  JsonProcessingException{
        if(principal !=null){
            UserModel user=userService.findByUsername(principal.getName());
            return ResponseEntity.ok().body(Helpers.convertToJson(UserConvert.convertToDto(user)));
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Status\": \"Not a user\"}");
        }
    }
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody UserPostDto userPostDto,Principal principal) throws JsonProcessingException{
        if(principal != null){
            UserModel user=userService.findByUsername(principal.getName());
            if(userPostDto.getEmail() != null && !userPostDto.getEmail().isEmpty()){
                if (!userService.existsByEmail(userPostDto.getEmail())){
                    user.setEmail(userPostDto.getEmail());
                }else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already in use !");
                }
            }
            if(userPostDto.getUsername() != null && !userPostDto.getUsername().isEmpty()){
                if (!userService.existsByUsername(userPostDto.getUsername())){
                    user.setUsername(userPostDto.getUsername());
                }else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already in use !");
                }
            }
            if(userPostDto.getFirstname()!=null){
                user.setFirstname(userPostDto.getFirstname());
            }
            if(userPostDto.getLastname()!=null){
                user.setLastname(userPostDto.getLastname());
            }
            if(userPostDto.getNumber()!=null){
                user.setPhone(userPostDto.getNumber());
            }
            if(userPostDto.getGender() !=null){
                user.setGender(userPostDto.getGender());
            }
            if(userPostDto.getDob() !=null){
                user.setDob(userPostDto.getDob());
            }
            userService.saveUser(user);
            return ResponseEntity.ok().body(Helpers.convertToJson(UserConvert.convertToDto(user)));
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Status\": \"Not a user\"}");

    }

    @PostMapping("/uploadPhotos")
    public ResponseEntity<String> uploadPhotos(
            MultipartFile profilePhoto,
            MultipartFile coverPhoto,
            @RequestParam("title") String title,
            Principal principal

            ){
        try {
            if(principal != null){
                UserModel user = userService.findByUsername(principal.getName());
                if( profilePhoto != null){
                    InputStream profilePhotoInputStream = profilePhoto.getInputStream();
                    String profilePhotoUrl = flickrService.savePhoto(profilePhotoInputStream, title);
                    user.setPhone(profilePhotoUrl);
                    userService.saveUser(user);
                    return ResponseEntity.ok().body("Uploaded and updated user's profile photo URL: " + profilePhotoUrl);
                }
                if (coverPhoto != null){
                    InputStream coverPhotoInputStream = coverPhoto.getInputStream();
                    String coverPhotoUrl = flickrService.savePhoto(coverPhotoInputStream, title);
                    user.setCoverUrl(coverPhotoUrl);
                    userService.saveUser(user);
                    return ResponseEntity.ok().body("Uploaded and updated user's cover photo URL: " + coverPhotoUrl);
                }
                else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Status\": \" Photo Not Existed \"}");
                }
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Status\": \"  Not a user \"}");
            }

        }catch (Exception ex) {
            log.error("Error uploading and updating photos: " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading and updating photos.");

        }

    }
    /*----------------Reviews--------------*/
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDto>> returnReviews(Principal principal){
        UserModel user = userService.findByUsername(principal.getName());
        return ResponseEntity.ok().body(reviewService.findByUser(user.getId()));
    }

    @PostMapping("/reviews")
    public ResponseEntity<String> createReview(@RequestBody ReviewDto reviewDto,Principal principal) throws JsonProcessingException {
        reviewDto.setUserId(userService.findByUsername(principal.getName()).getId());
        return ResponseEntity.ok().body(Helpers.convertToJson(reviewService.save(reviewDto)));
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<String> updateReview(@PathVariable("id") Long id, @RequestBody ReviewDto reviewDto,Principal principal ) throws Exception {
        ReviewDto reviewDto1 = reviewService.findById(id);
        if (reviewDto1 != null && reviewDto1.getUserId() == userService.findByUsername(principal.getName()).getId()){
            if (reviewDto.getComment() != null) {
                reviewDto1.setComment(reviewDto.getComment());
            }
            if (reviewDto.getRating() != 0) {
                reviewDto1.setRating(reviewDto.getRating());
            }

            return ResponseEntity.ok().body(Helpers.convertToJson(reviewService.save(reviewDto1)));
        }

        else
            return ResponseEntity.badRequest().body("{\"Status\": \"Review not found Or not authorized  \"}");
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReviewById(@PathVariable("id") Long id){
        reviewService.deleteById(id);
        return ResponseEntity.ok().body("{\"Status\": \"Successful Deletion\"}");
    }

}
