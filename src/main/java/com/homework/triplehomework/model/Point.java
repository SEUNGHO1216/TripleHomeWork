package com.homework.triplehomework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Point {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    private Action action;

    private int point;

    public Point(User user, Action action, int point){
        this.user = user;
        this.action = action;
        this.point = point;
    }
}
