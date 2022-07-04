package com.homework.triplehomework.dto;

import com.homework.triplehomework.model.Place;
import com.homework.triplehomework.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaceUserDto {

    private Place place;
    private User user;
}
