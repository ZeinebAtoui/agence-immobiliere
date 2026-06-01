package com.example.AgenceImmobilier.services.logementS;


import com.example.AgenceImmobilier.DTOs.request.LogementParameters;
import com.example.AgenceImmobilier.DTOs.response.LogementDto;
import com.example.AgenceImmobilier.models.logement.Logement;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface LogementService {
    LogementDto findDtoById(Long id) throws Exception;
    List<LogementDto> findAll();
    List<LogementDto> findByHost(Long id);
    List<Logement> findWithParametersBasic(String country, String city, Date startDate, Date endDate, int guests);
    List<LogementDto> findWithParameters(LogementParameters logementParameters);

    Logement findById(Long id);
    LogementDto save(LogementDto logement) throws Exception;
    LogementDto update(Long id,LogementDto logementDto) throws Exception;
    void deleteById(Long id);

    Page<LogementDto> searchLogements(LogementParameters parameters, int page, int pageSize);

}
