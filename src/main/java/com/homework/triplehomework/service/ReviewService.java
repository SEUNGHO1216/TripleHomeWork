package com.homework.triplehomework.service;

import com.homework.triplehomework.dto.PlaceUserDto;
import com.homework.triplehomework.dto.ReviewDto;
import com.homework.triplehomework.model.*;
import com.homework.triplehomework.repository.*;
import com.homework.triplehomework.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PointRepository pointRepository;

    @Transactional
    //action 값에 따른 포인트 적립 로직 작성
    public ResponseEntity<String> pointEvent(ReviewDto reviewDto, UserDetailsImpl userDetails) {
        String action = reviewDto.getAction();
        switch (action){
            case "ADD":
                return reviewAdd(reviewDto, userDetails);
            case "MOD":
                return reviewMod(reviewDto, userDetails);
            case "DELETE":
                return reviewDelete(reviewDto, userDetails);
            default:
                return ResponseEntity.status(400).body("리뷰에 대한 작업을 선택해주세요(ADD, MOD, DELETE)");
        }
    }

    private PlaceUserDto exceptionCheck(ReviewDto reviewDto, UserDetailsImpl userDetails){
        //존재 하는 장소에 리뷰를 남길 수 있도록 예외처리
        Place place = placeRepository.findByPlaceId(reviewDto.getPlaceId()).orElseThrow(
                ()->new NullPointerException("리뷰를 남길 장소가 없습니다. 올바른 장소에 리뷰를 남겨주세요")
        );
        //존재하는 유저인지 확인
        User user = userRepository.findByUserId(reviewDto.getUserId()).orElseThrow(
                ()->new NullPointerException("회원 정보가 없습니다")
        );
        //로그인한 유저가 남기는 것인지 확인
        if(!user.getUserId().equals(userDetails.getUser().getUserId())){
            throw new IllegalArgumentException("현재 로그인한 유저와 리뷰를 남긴 유저가 같지 않습니다");
        }
        //아무것도 입력하지 않고 작성 , 수정 시 예외처리
        if(reviewDto.getContent().isBlank() && reviewDto.getAttachedPhotoIds().isEmpty()){
            throw new IllegalArgumentException("내용을 1글자 이상 남기거나, 사진을 1장 이상 첨부해야 합니다");
        }
        return new PlaceUserDto(place, user);
    }
    //리뷰 작성에 따른 로직 수행
    @Transactional
    protected ResponseEntity<String> reviewAdd(ReviewDto reviewDto, UserDetailsImpl userDetails){

        PlaceUserDto placeUserDto = exceptionCheck(reviewDto, userDetails);
        Place place = placeUserDto.getPlace();
        User user = placeUserDto.getUser();

        //한 유저가 장소 하나 당 하나의 리뷰만 남길 수 있도록 예외처리
        Review writtenReview = reviewRepository.findByPlaceAndUser(place, user).orElse(null);
        int point=0;
        if(writtenReview != null){
            throw new IllegalArgumentException("장소당 하나의 리뷰만 작성 가능합니다");
        }else{
            //정상적인 리뷰 생성에 따른 point 적립
            String content = reviewDto.getContent();
            //첫 댓글인지 파악 후 포인트 부여
            boolean isFirst = false;
            boolean isNotFirst = reviewRepository.existsByPlace(place);
            //첫 댓글일 경우
            if(!isNotFirst){
                isFirst = true;
            }
            Review review = new Review(content, isFirst, place, user);
            reviewRepository.save(review);

            List<String> attachedPhotoIds = reviewDto.getAttachedPhotoIds();
            attachedPhotoIds.stream().forEach(photoId -> imageRepository.save(new Image(photoId, review, user)));

            //리뷰 생성 시 포인트 적립
            point = pointAddCalculate(reviewDto, isFirst, user);
        }
        return ResponseEntity.status(200).body("200, 포인트 : "+point);
    }

    //리뷰 수정에 따른 로직 수행
    @Transactional
    protected ResponseEntity<String> reviewMod(ReviewDto reviewDto, UserDetailsImpl userDetails){

        PlaceUserDto placeUserDto = exceptionCheck(reviewDto, userDetails);
        Place place = placeUserDto.getPlace();
        User user = placeUserDto.getUser();
        //수정할 리뷰글을 reviewId로 찾음
        Review review= reviewRepository.findByReviewId(reviewDto.getReviewId()).orElse(null);
        int point=0;
        if(review == null){
            throw new NullPointerException("해당 리뷰가 없습니다");
        }else{
            //리뷰 수정 시 포인트 적립
            point = pointModCalculate(reviewDto, user);
            //리뷰 수정에 따른 point 적립
            String content = reviewDto.getContent();
            review.update(content);
            reviewRepository.save(review);

            //새로 수정된 이미지 id
            List<String> attachedPhotoIds = reviewDto.getAttachedPhotoIds();

            //기존 이미지 삭제하고 새로 들어온 이미지 추가하는 방식
            imageRepository.deleteAllByReview(review);

            attachedPhotoIds.stream().forEach(photoId -> imageRepository.save(new Image(photoId, review, user)));

        }
        return ResponseEntity.status(200).body("200, 포인트 : "+point);

    }

    //리뷰 삭제에 따른 로직 수행
    @Transactional
    protected ResponseEntity<String> reviewDelete(ReviewDto reviewDto, UserDetailsImpl userDetails){

        PlaceUserDto placeUserDto = exceptionCheck(reviewDto, userDetails);
        Place place = placeUserDto.getPlace();
        User user = placeUserDto.getUser();
        //삭제힐 리뷰글을 reviewId로 찾음
        Review review= reviewRepository.findByReviewId(reviewDto.getReviewId()).orElse(null);
        int point=0;
        if(review == null){
            throw new NullPointerException("삭제할 리뷰가 없습니다");
        }else{

            //첫 게시물 여부 확인 후 포인트에서 차감
            boolean isFirst = review.isFirst();
            //리뷰 삭제 시 포인트 차감
            point = pointDeleteCalculate(reviewDto, isFirst, user);
            //리뷰 삭제
            reviewRepository.deleteByReviewId(reviewDto.getReviewId());

        }
        return ResponseEntity.status(200).body("200, 포인트 : "+point);

    }

    private int getPoint(User user){
        //유저의 가장 최근 포인트를 불러옴
        Point point = pointRepository.findFirstByUserOrderByIdDesc(user).orElse(null);
        int pointCount = 0;
        if(point == null){
            pointCount = 0;
        }else{
            pointCount = point.getPoint();
        }
        return pointCount;
    }

    //add 땐 더하기만 해주면 된다
    @Transactional
    protected int pointAddCalculate(ReviewDto reviewdto, boolean isFirst, User user){

        int pointCount = getPoint(user);
        //1자 이상 리뷰 작성시 포인트 부여
        if(!reviewdto.getContent().isBlank()){
            pointCount++;
        }
        //1장 이상의 사진 등록시 포인트 부여
        if(reviewdto.getAttachedPhotoIds().size()>0){
            pointCount++;
        }
        //첫 댓글인지 파악 후 포인트 부여
        if(isFirst){
            pointCount++;
        }

        //포인트 적립
        Point pointAdd = new Point(user, Action.ADD, pointCount);
        pointRepository.save(pointAdd);

        return pointAdd.getPoint();

    }

    //mod 일 땐 리뷰id로 조회 후 기존 것과 새로 들어온 것 비교
    @Transactional
    protected int pointModCalculate(ReviewDto reviewDto, User user){

        int pointCount = getPoint(user);
        //기존 리뷰 내역 조회
        Review review = reviewRepository.findByReviewId(reviewDto.getReviewId()).orElse(null);

        if(review == null){
            throw new NullPointerException("수정할 리뷰가 없습니다");
        }
        String oldContent = review.getContent();
        String newContent = reviewDto.getContent();

        //기존에 내용 없었는데 수정 후 추가 됐다면 +1, 기존에 내용을 작성했는데 수정 후 내용 없다면 -1
        if(oldContent.isBlank() && !newContent.isBlank()) {
            pointCount++;
        }else if(!oldContent.isBlank() && newContent.isBlank()){
            pointCount--;
        }

        List<Image> images = imageRepository.findAllByReview(review);
        List<String> oldPhotoIds = new ArrayList<>();
        for(Image image: images){
            oldPhotoIds.add(image.getPhotoId());
        }
        List<String> newPhotoIds = reviewDto.getAttachedPhotoIds();

        //기존에 내용 없었는데 수정 후 추가 됐다면 +1, 기존에 내용을 작성했는데 수정 후 내용 없다면 -1
        if(oldPhotoIds.isEmpty() && !newPhotoIds.isEmpty()){
            pointCount++;
        }else if(!oldPhotoIds.isEmpty() && newPhotoIds.isEmpty()){
            pointCount--;
        }
        //수정 시에는 첫 댓글인지 파악 후 포인트 부여 고려하지 않는다(생성 시에만 고려)

        //포인트 수정(이력에 남기기 위해 매번 이력 생성)
        Point pointMod = new Point(user, Action.MOD, pointCount);
        pointRepository.save(pointMod);

        return pointMod.getPoint();
    }

    @Transactional
    protected int pointDeleteCalculate(ReviewDto reviewDto, boolean isFirst, User user){

        int pointCount = getPoint(user);

        //기존에 내용이 1글자 이상 있는 리뷰를 지우면 포인트 차감
        if(!reviewDto.getContent().isBlank()){
            pointCount--;
        }
        //기존에 사진이 1장 이상 있는 리뷰를 지우면 포인트 차감
        if(reviewDto.getAttachedPhotoIds().size()>0){
            pointCount--;
        }

        //삭제 시, 첫 댓글이었던 것을 지우면 포인트 차감
        if(isFirst){
            pointCount--;
        }
        //포인트 삭제(이력에 남기기 위해 매번 이력 생성)
        Point pointDelete = new Point(user, Action.DELETE, pointCount);
        pointRepository.save(pointDelete);

        return pointDelete.getPoint();
    }




    //포인트 조회, 최신 10개 내역만 나오도록 페이징 처리
    public ResponseEntity<List<Point>> showPoint(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        //정렬 기준 설정
        Sort.Direction direction = Sort.Direction.DESC;
        System.out.println(direction);
        Sort sort = Sort.by(direction, "id");

        int page = 0;
        int size = 10;
        Pageable pageable= PageRequest.of(page, size, sort);

        Page<Point> points = pointRepository.findAllByUser(user, pageable);

        //페이징 처리 중 내용물만 뽑아냄
        List<Point> pointList = points.getContent();

        return ResponseEntity.status(200).body(pointList);
    }
}
