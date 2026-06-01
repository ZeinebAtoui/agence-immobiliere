package com.example.AgenceImmobilier.services.logementS;

import com.example.AgenceImmobilier.models.logement.Media;
import com.example.AgenceImmobilier.repositories.logementR.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaService {
    @Autowired
    MediaRepository mediaRepository;

    public Media saveMedia(Media media){
        return mediaRepository.save(media);
    }
}
