package com.example.AgenceImmobilier.controllers;

import com.example.AgenceImmobilier.DTOs.response.BookingDto;
import com.example.AgenceImmobilier.DTOs.response.LogementDto;
import com.example.AgenceImmobilier.DTOs.response.ReviewDto;
import com.example.AgenceImmobilier.converter.LogementConverter;
import com.example.AgenceImmobilier.models.logement.Logement;
import com.example.AgenceImmobilier.models.logement.Media;
import com.example.AgenceImmobilier.models.user.UserModel;
import com.example.AgenceImmobilier.services.bookingS.BookingService;
import com.example.AgenceImmobilier.services.flicker.FlickrService;
import com.example.AgenceImmobilier.services.logementS.LogementService;
import com.example.AgenceImmobilier.services.logementS.MediaService;
import com.example.AgenceImmobilier.services.logementS.ReviewService;
import com.example.AgenceImmobilier.services.user.UserService;
import com.example.AgenceImmobilier.utils.Helpers;
import com.example.AgenceImmobilier.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/host")
@PreAuthorize("hasRole('ROLE_HOST') or hasRole('ROLE_ADMIN')")
public class HostController {
    @Autowired
    private UserService userService;
    @Autowired
    LogementService logementService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    FlickrService flickrService;
    @Autowired
    BookingService bookingService;
    @Autowired
    MediaService mediaService;
    @Autowired
    ReviewService reviewService;
    /*----------Logement------------*/
    @PostMapping("/logement")
    public ResponseEntity<LogementDto> createLogement(@RequestBody LogementDto logementDto) throws Exception {
        System.out.println(logementDto);

        return ResponseEntity.ok().body(logementService.save(logementDto));

    }

    @GetMapping("/logement")
    public ResponseEntity<List<LogementDto>> returnMyLogements(Principal principal){
        UserModel user = userService.findByUsername(principal.getName());
        return ResponseEntity.ok().body(logementService.findByHost(user.getId()));
    }

    @GetMapping("/logement/{id}")
    public ResponseEntity<String> returnLogementById(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok().body(Helpers.convertToJson(logementService.findDtoById(id)));
    }

    @PutMapping("/logement/{id}")
    public ResponseEntity<String> updateLogement(@PathVariable("id") Long id, @RequestBody LogementDto logementDto) throws Exception {
        LogementDto logementDto1=logementService.findDtoById(id);

        if(logementDto!=null &&  logementDto1!=null && logementDto1.getHost().getId() == tokenUtils.ExtractId()) {

            return ResponseEntity.ok().body(Helpers.convertToJson(logementService.update(id,logementDto)));
        }
        else
            return ResponseEntity.ok().body("{\"Status\": \"Logement not found\"}");
    }

    @DeleteMapping("/logement/{id}")
    public ResponseEntity<String> deleteLogementById(@PathVariable("id") Long id){
        logementService.deleteById(id);
        return ResponseEntity.ok().body("{\"Status\": \"Successful Deletion\"}");
    }
    /*----------Booking------------*/
    @GetMapping("/listings/{id}/bookings")
    public ResponseEntity<List<BookingDto>> returnLogementBookings(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(bookingService.returnLogementBookings(id));
    }

    /*---------UploadPhotos------------*/
    @PostMapping("/{logementId}/upload-photos")
    public ResponseEntity<String> uploadPhotos(
            @PathVariable Long logementId,
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("files") List<MultipartFile> files) throws IOException {

        try {
            Logement logement = logementService.findById(logementId);
            if (logement != null) {

                if (!type.equals("photo") && !type.equals("video")) {
                    return ResponseEntity.badRequest().build();
                }

                List<Media> savedMediaList = new ArrayList<>();

                // Parcourez la liste de fichiers et sauvegardez chaque média
                for (MultipartFile file : files) {
                    InputStream fileInputStream = file.getInputStream();
                    String mediaUrl = flickrService.savePhoto(fileInputStream, title);

                    // Créez un objet Media pour chaque fichier et associez-le au logement
                    Media media = new Media();
                    media.setTitle(title);
                    media.setUrl(mediaUrl);
                    media.setType(type);
                    media.setLogement(logement);

                    // Sauvegardez le média dans la base de données et ajoutez-le à la liste
                    Media savedMedia = mediaService.saveMedia(media);
                    savedMediaList.add(savedMedia);
                }

                return ResponseEntity.ok(Helpers.convertToJson(LogementConverter.convertToDto(logement)));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*--------------Reviews---------------*/
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDto>> returnReviews(Principal principal){
        UserModel user = userService.findByUsername(principal.getName());
        return ResponseEntity.ok().body(reviewService.findByHost(user.getId()));
    }

}
