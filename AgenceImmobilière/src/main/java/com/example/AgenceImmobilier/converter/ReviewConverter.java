package com.example.AgenceImmobilier.converter;

import com.example.AgenceImmobilier.DTOs.response.ReviewDto;
import com.example.AgenceImmobilier.models.logement.Review;
import com.example.AgenceImmobilier.services.logementS.LogementService;
import com.example.AgenceImmobilier.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter {
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

    public static ReviewDto convertToDto(Review review){
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setId(review.getId());
        reviewDto.setComment(review.getComment());
        reviewDto.setDate(review.getDate());
        reviewDto.setRating(review.getRating());

        reviewDto.setLogementId(review.getLogement().getId());
        reviewDto.setLogementTitle(review.getLogement().getTitle());
        reviewDto.setUserId(review.getUser().getId());
        reviewDto.setUserName(review.getUser().getUsername());

        return reviewDto;
    }

    public static Review convert(ReviewDto reviewDto){
        Review review = new Review();

        review.setId(reviewDto.getId());
        review.setComment(reviewDto.getComment());
        review.setDate(reviewDto.getDate());
        review.setRating(reviewDto.getRating());

        review.setLogement(logementServiceStatic.findById(reviewDto.getLogementId()));
        review.setUser(userServiceStatic.findById(reviewDto.getUserId()));

        return review;
    }
}
