package com.homework.triplehomework.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reviewId;
    private String content;

    private boolean isFirst;
//    @OneToMany(mappedBy = "review")
//    private List<String> attachedPhotoIds;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    public Review(String content, boolean isFirst, Place place, User user){
        this.reviewId = UUID.randomUUID().toString();
        this.content = content;
        this.isFirst = isFirst;
        this.place = place;
        this.user = user;

    }
    public void update(String content){
        this.content = content;
    }

}
