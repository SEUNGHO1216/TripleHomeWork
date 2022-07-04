package com.homework.triplehomework.repository;

import com.homework.triplehomework.model.Place;
import com.homework.triplehomework.model.Review;
import com.homework.triplehomework.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //한 장소당 하나의 리뷰만 남기게 하기 위함
    Optional<Review> findByPlaceAndUser(Place place, User user);
    //첫 리뷰인지 확인하기 위함
    boolean existsByPlace(Place place);
    //수정 또는 삭제 시 해당 리뷰 확인하기 위함
    Optional<Review> findByReviewId(String reviewId);
    //리뷰 삭제
    void deleteByReviewId(String reviewId);
}
