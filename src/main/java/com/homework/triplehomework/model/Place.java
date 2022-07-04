package com.homework.triplehomework.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;


@Getter
@Entity
@Table(indexes = {@Index(name = "place_index",columnList = "placeId")})
public class Place {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeId;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<Review> review;

    public Place(){
        this.placeId= UUID.randomUUID().toString();
    }
}
