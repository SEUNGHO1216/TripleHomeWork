package com.homework.triplehomework.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photoId;

//    private String filename;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    public Image(String photoId, Review review, User user){
        this.photoId = photoId;
        this.review = review;
        this.user = user;
    }
    public void update(String photoId){
        this.photoId = photoId;
    }
}
