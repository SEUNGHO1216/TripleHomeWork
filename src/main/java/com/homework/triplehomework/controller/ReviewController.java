package com.homework.triplehomework.controller;

import com.homework.triplehomework.dto.ReviewDto;
import com.homework.triplehomework.model.Point;
import com.homework.triplehomework.security.UserDetailsImpl;
import com.homework.triplehomework.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 작성에 따른 포인트 적립 API
    @PostMapping("/events")
    public ResponseEntity<String> pointEvent(@RequestBody ReviewDto reviewDto,
                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        return  reviewService.pointEvent(reviewDto, userDetails);
    }

    //포인트 조회 API (10개 페이지 처리)
    @GetMapping("/events")
    public ResponseEntity<List<Point>> showPoint(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.showPoint(userDetails);
    }

}
