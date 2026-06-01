package com.example.AgenceImmobilier.services.logementS;

import com.example.AgenceImmobilier.DTOs.response.ReviewDto;
import com.example.AgenceImmobilier.converter.ReviewConverter;
import com.example.AgenceImmobilier.exceptions.EntityNotFoundException;
import com.example.AgenceImmobilier.models.logement.Logement;
import com.example.AgenceImmobilier.models.logement.Review;
import com.example.AgenceImmobilier.repositories.logementR.LogementRepository;
import com.example.AgenceImmobilier.repositories.logementR.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImp implements ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private LogementRepository logementRepository;

    @Override
    public ReviewDto findById(Long id) throws Exception {
        Review review;
        review=reviewRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Review not found with id : "+id));
        return ReviewConverter.convertToDto(review);
    }

    @Override
    public List<ReviewDto> findAll() {
        return reviewRepository.findAll()
                .stream()
                .map(ReviewConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDto> findByHost(Long id) {
        return reviewRepository.findAllByLogementHostId(id)
                .stream()
                .map(ReviewConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDto> findByUser(Long id) {
        return reviewRepository.findAllByUserId(id)
                .stream()
                .map(ReviewConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDto save(ReviewDto reviewDto) {
        Review review=ReviewConverter.convert(reviewDto);
        review=reviewRepository.save(review);

        System.out.println("Review added or updated ");

        Logement logement=logementRepository.findById(review.getLogement().getId())
                .orElseThrow(()->new EntityNotFoundException("Logement not found with id :"+ reviewDto.getLogementId()));

        double newNotMoy = (logement.getAverageRating()*logement.getNumOfReviews() + review.getRating())/(logement.getNumOfReviews()+1);
        logement.setAverageRating(newNotMoy);

        logement.setNumOfReviews(logement.getNumOfReviews()+1);

        logementRepository.save(logement);

        return ReviewConverter.convertToDto(review);
    }

    @Override
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }
}
