package com.homework.triplehomework.repository;

import com.homework.triplehomework.model.Image;
import com.homework.triplehomework.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    //해당 리뷰에 있는 모든 이미지 불러오기
    List<Image> findAllByReview(Review review);

    //이미지 수정 시 사용
    void deleteAllByReview(Review review);
}
