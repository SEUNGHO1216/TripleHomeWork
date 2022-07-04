### Triple Assignment for job process

- skill : java11, springboot 2.7.1, AWS MySQL RDS, JWT, Spring Data JPA
- purpose : Build business logic for managing point by making review


- ## 개발 포인트
    - 트리플 채용팀 문의 결과, 뷰 페이지가 없이 포인트를 적립하고(post, /events), 조회하는 API 2가지만 서버로 구현하는 것이 과제
    - 예제로 주어진 json 형태의 데이터로 정보 교환 하는 것이 포인트 
  ```
  POST /events
  
  {
    "type": "REVIEW",
    "action": "ADD", /* "MOD", "DELETE" */
    "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
    "content": " !",
    "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-
    851d-4a50-bb07-9cc15cbdc332"],
    "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
  }
    ```
    - 인덱싱 및 각종 요구사항 수행


- ## 실행 방법
사용자 별 리뷰 작성 및 포인트 관리를 위해 회원가입 및 로그인 api를 추가로 구현함(jwt)

1. 회원가입 및 로그인 진행
```
회원가입
post, /user/signup

{
    "username": "seungho",
    "password": "12345"
}
```
```
로그인
post, /user/login
{
    "username": "seungho",
    "password": "12345"
}
```
2. 리뷰를 남길 장소 생성
```
장소생성
post, /place

데이터 없음
```
3. 리뷰 생성, 수정, 삭제
- postman 테스트 시 header 값으로 Authorization에 jwt 토큰을 넣어줘야함(로그인 시 response header의 Bearer ~ )
- 생성 시(ADD)에는 reviewId가 없는 시점
- attachedPhotoIds는 실제로 이미지 첨부기능은 미구현, 하나의 문자열이 하나의 이미지라고 가정(문자열은 아무거나 가능)
- userId 와 placeId는 회원가입한 유저의 uuid 값과 생성한 장소의 uuid값을 대입
- 수정 시(MOD)에는 reviewId도 작성한 리뷰의 uuid 값 대입 
```
리뷰 작업에 따른 포인트 증감
ex)
post, /events

{
    "type": "REVIEW",
    "action": "ADD", 
    "reviewId": "5ca560d3-7740-49cd-b6f7-2e7e51f55440(생성 시는 의미 없음)",
    "content": "트리플 좋아요!",
    "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-
    851d-4a50-bb07-9cc15cbdc332"],
    "userId": "7571a40a-c57d-4510-b27b-5130208113ed",
    "placeId": "1df61ed5-814f-4161-be92-4bc8db3497fd"
}
```
4. 포인트 조회
- postman 테스트 시 header 값으로 Authorization에 jwt 토큰을 넣어줘야함(로그인 시 response header의 Bearer ~ )
- 포인트 증감 데이터가 엄청나게 많을 시를 대비하여 최근 10개만 보여주도록 페이징 처
```
get, /events

데이터 없음
```

***바로 적용 가능한 데이터 샘플***
```
리뷰 생성
post, /events

{
    "type": "REVIEW",
    "action": "ADD", 
    "reviewId": "5ca560d3-7740-49cd-b6f7-2e7e51f55440(생성 시는 의미 없음)",
    "content": "트리플 좋아요!",
    "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-
    851d-4a50-bb07-9cc15cbdc332"],
    "userId": "fbbd2622-7384-46be-882e-9175451ae781",
    "placeId": "04403ab2-7980-4201-b6fe-2afb24e63cee"
}
```
수정, 삭제부터는 실제 데이터베이스 저장된 값을 대입하며 테스트 해야합니다.

