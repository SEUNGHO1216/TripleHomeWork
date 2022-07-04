package com.homework.triplehomework.controller;

import com.homework.triplehomework.model.Place;
import com.homework.triplehomework.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PlaceController {

    private final PlaceRepository placeRepository;

    @PostMapping("/place")
    public ResponseEntity<String> makePlace(){
        Place place =new Place();
        placeRepository.save(place);
        return ResponseEntity.status(200).body("placeId : "+place.getPlaceId());
    }

}
