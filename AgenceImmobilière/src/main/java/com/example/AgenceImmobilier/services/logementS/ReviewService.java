package com.example.AgenceImmobilier.services.logementS;

import com.example.AgenceImmobilier.DTOs.response.ReviewDto;

import java.util.List;

public interface ReviewService {
    ReviewDto findById(Long id) throws Exception;
    List<ReviewDto> findAll();
    List<ReviewDto> findByHost(Long id);
    List<ReviewDto> findByUser(Long id);
    ReviewDto save(ReviewDto reviewDto);

    void deleteById(Long id);
}
