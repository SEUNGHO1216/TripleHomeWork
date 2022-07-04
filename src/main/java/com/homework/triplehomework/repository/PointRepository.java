package com.homework.triplehomework.repository;

import com.homework.triplehomework.model.Point;
import com.homework.triplehomework.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    //유저의 가장 최신 포인트를 불러옴
    Optional<Point> findFirstByUserOrderByIdDesc(User user);

    //최신 10개에 대한 포인트 증감 이력을 보여줌
    Page<Point> findAllByUser(User user, Pageable pageable);
}
