package com.example.AgenceImmobilier.services.logementS;

import com.example.AgenceImmobilier.DTOs.request.LogementParameters;
import com.example.AgenceImmobilier.DTOs.response.LogementDto;
import com.example.AgenceImmobilier.converter.LogementConverter;
import com.example.AgenceImmobilier.exceptions.EntityNotFoundException;
import com.example.AgenceImmobilier.models.logement.Logement;
import com.example.AgenceImmobilier.repositories.bookingR.BookingRepository;
import com.example.AgenceImmobilier.repositories.logementR.LogementRepository;
import com.example.AgenceImmobilier.services.user.UserService;
import com.example.AgenceImmobilier.utils.TokenUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.example.AgenceImmobilier.converter.LogementConverter.convertToDto;

@Service
public class LogementServiceImp implements LogementService{

    LogementConverter logementConverter = new LogementConverter();

    @Autowired
    private LogementRepository logementRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    UserService userService;


    @Override
    public LogementDto findDtoById(Long id) throws Exception {
        Logement logement;

            logement = logementRepository.findById(id)
                    .orElseThrow(()->new EntityNotFoundException("Logement not found with id : " + id));


        return convertToDto(logement);
    }

    @Override
    public List<LogementDto> findAll() {
        return logementRepository.findAll()
                .stream()
                .map(LogementConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LogementDto> findByHost(Long id) {
        return logementRepository.findAllByHostId(id)
                .stream()
                .map(LogementConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Logement> findWithParametersBasic(String country, String city, Date startDate, Date endDate, int guests) {
        List<Logement> logementList= logementRepository.findAllByCountryAndCityAndStartDateBeforeAndEndDateAfterAndMaxGuestsIsGreaterThanEqualOrderByMinCost(country, city, startDate, endDate, guests);

        logementList = logementList.stream().map(logement -> {
            if(logement.getBookings().isEmpty() || bookingRepository.findAllByLogementIdAndDateAfterAndAndDateBefore(logement.getId(), startDate, endDate).isEmpty())
                return logement;
            else
                return null;
        }).collect(Collectors.toList());

        while(logementList.remove(null));
        if(logementList.isEmpty() || logementList==null)
            return Collections.emptyList();

        return logementList;
    }

    @Override
    public List<LogementDto> findWithParameters(LogementParameters logementParameters) {
        List<Logement> logementList=findWithParametersBasic(logementParameters.getCountry(), logementParameters.getCity(), logementParameters.getStartDate(), logementParameters.getEndDate(), logementParameters.getGuests());

        if(logementList.isEmpty())
            return Collections.emptyList();

        if(logementParameters.getType()!=null){
            logementList = logementList.stream().map(logement -> {
                if(logement.getType()==logementParameters.getType())
                    return logement;
                else
                    return null;
            }).collect(Collectors.toList());

            while(logementList.remove(null));
            if(logementList.isEmpty())
                return Collections.emptyList();
        }
        if(logementParameters.getMaxCost()!=null){
            logementList = logementList.stream().map(logement -> {
                if((logement.getMinCost()+logementParameters.getGuests()*logement.getCostPerExtraGuest())<logementParameters.getMaxCost())
                    return logement;
                else
                    return null;
            }).collect(Collectors.toList());

            while(logementList.remove(null));
            if(logementList.isEmpty())
                return Collections.emptyList();
        }
        if(logementParameters.getWifi()!=null){
            logementList = logementList.stream().map(logement -> {
                if(logement.isWifi()==logementParameters.getWifi())
                    return logement;
                else
                    return null;
            }).collect(Collectors.toList());

            while(logementList.remove(null));
            if(logementList.isEmpty())
                return Collections.emptyList();
        }
        if(logementParameters.getAc()!=null){
            logementList = logementList.stream().map(logement -> {
                if(logement.isAc()==logementParameters.getAc())
                    return logement;
                else
                    return null;
            }).collect(Collectors.toList());

            while(logementList.remove(null));
            if(logementList.isEmpty())
                return Collections.emptyList();
        }
        if(logementParameters.getHeating()!=null){
            logementList = logementList.stream().map(logement -> {
                if(logement.isHeating()==logementParameters.getHeating())
                    return logement;
                else
                    return null;
            }).collect(Collectors.toList());

            while(logementList.remove(null));
            if(logementList.isEmpty())
                return Collections.emptyList();
        }
        if(logementParameters.getKitchen()!=null){
            logementList = logementList.stream().map(logement -> {
                if(logement.isKitchen()==logementParameters.getKitchen())
                    return logement;
                else
                    return null;
            }).collect(Collectors.toList());

            while(logementList.remove(null));
            if(logementList.isEmpty())
                return Collections.emptyList();
        }
        if(logementParameters.getTv()!=null){
            logementList = logementList.stream().map(logement -> {
                if(logement.isTv()==logementParameters.getTv())
                    return logement;
                else
                    return null;
            }).collect(Collectors.toList());

            while(logementList.remove(null));
            if(logementList.isEmpty())
                return Collections.emptyList();
        }
        if(logementParameters.getParking()!=null){
            logementList = logementList.stream().map(logement -> {
                if(logement.isParking()==logementParameters.getParking())
                    return logement;
                else
                    return null;
            }).collect(Collectors.toList());

            while(logementList.remove(null));
            if(logementList.isEmpty())
                return Collections.emptyList();
        }
        if(logementParameters.getElevator()!=null){
            logementList = logementList.stream().map(logement -> {
                if(logement.isElevator()==logementParameters.getElevator())
                    return logement;
                else
                    return null;
            }).collect(Collectors.toList());

            while(logementList.remove(null));
            if(logementList.isEmpty())
                return Collections.emptyList();
        }


        while(logementList.remove(null));
        if(logementList.isEmpty() || logementList==null)
            return Collections.emptyList();

        return logementList.stream()
                .map(LogementConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Logement findById(Long id) {
        Logement logement;
        logement = logementRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Logement not found with id : "+id));
        return logement;
    }



    @Override
    public LogementDto save(LogementDto logementDto) throws Exception {
        Logement logement = LogementConverter.convert(logementDto);
        logement.setHost(userService.findById(tokenUtils.ExtractId()));
        logement = logementRepository.save(logement);

        System.out.println("logement added or updated");
        return convertToDto(logement);
    }

    @Override
    public LogementDto update(Long id,LogementDto logementDto) throws Exception {
        LogementDto logementDto1=this.findDtoById(id);

            logementDto1.setStartDate(logementDto.getStartDate());
            logementDto1.setEndDate(logementDto.getEndDate());
            logementDto1.setMaxGuests(logementDto.getMaxGuests());
            logementDto1.setCountry(logementDto.getCountry());
            logementDto1.setCity(logementDto.getCity());
            logementDto1.setType(logementDto.getType());
            logementDto1.setMinCost(logementDto.getMinCost());
            logementDto1.setWifi(logementDto.isWifi());
            logementDto1.setAc(logementDto.isAc());
            logementDto1.setHeating(logementDto.isHeating());
            logementDto1.setKitchen(logementDto.isKitchen());
            logementDto1.setTv(logementDto.isTv());
            logementDto1.setParking(logementDto.isParking());
            logementDto1.setElevator(logementDto.isElevator());
        logementRepository.save(LogementConverter.convert(logementDto1));
            return  logementDto1;

    }

    @Override
    public void deleteById(Long id) {
        logementRepository.deleteById(id);
    }

    @Override
    public Page<LogementDto> searchLogements(LogementParameters parameters, int page, int pageSize) {
        Specification<Logement> spec = buildSpecification(parameters);

        PageRequest pageRequest = PageRequest.of(page, pageSize);

        Page<LogementDto> result = logementRepository.findAll(spec, pageRequest)
                .map(logement -> convertToDto(logement)); // Convert Logement entities to LogementDto objects

        return result;
    }
    private Specification<Logement> buildSpecification(LogementParameters parameters) {
        return (root, query, criteriaBuilder) -> {
            // Initialize with a predicate that always evaluates to true
            Predicate predicate = criteriaBuilder.isTrue(criteriaBuilder.literal(true));

            if (parameters.getStartDate() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("startDate"), parameters.getStartDate()));
            }

            if (parameters.getEndDate() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("endDate"), parameters.getEndDate()));
            }

            if (parameters.getGuests() > 0) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("guests"), parameters.getGuests()));
            }

            if (parameters.getCountry() != null && !parameters.getCountry().isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("country"), parameters.getCountry()));
            }

            if (parameters.getCity() != null && !parameters.getCity().isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("city"), parameters.getCity()));
            }

            if (parameters.getType() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("type"), parameters.getType()));
            }

            if (parameters.getMaxCost() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("cost"), parameters.getMaxCost()));
            }

            if (parameters.getWifi() != null && parameters.getWifi()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("wifi")));
            }

            if (parameters.getAc() != null && parameters.getAc()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("ac")));
            }

            if (parameters.getHeating() != null && parameters.getHeating()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("heating")));
            }

            if (parameters.getKitchen() != null && parameters.getKitchen()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("kitchen")));
            }

            if (parameters.getTv() != null && parameters.getTv()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("tv")));
            }

            if (parameters.getParking() != null && parameters.getParking()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("parking")));
            }

            if (parameters.getElevator() != null && parameters.getElevator()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("elevator")));
            }

            return predicate;
        };
    }
}
