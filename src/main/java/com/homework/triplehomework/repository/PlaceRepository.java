package com.homework.triplehomework.repository;

import com.homework.triplehomework.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByPlaceId(String placeId);
}
