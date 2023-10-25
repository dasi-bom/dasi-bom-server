# 1️⃣ 개발 과정 살펴보기

- [cursor 기반 pagination으로 무한스크롤 구현하기](https://ming412.tistory.com/161)
- [모델과 DTO 간 변환을 다루는 방법에는 어떤 것들이 있을까? (정적 팩토리 메서드/Assembler/ModelMapper)](https://ming412.tistory.com/162)
- [@ControllerAdvice를 이용하여 중앙 집중식으로 예외 처리하기](https://ming412.tistory.com/212)

# 2️⃣ 프로젝트 구조

```
com
 ㄴ example
     ㄴ server
         ㄴ domain                                   # 도메인 별 컨트롤러, 서비스, 레포지토리 코드를 각각 정의
         |   ㄴ ...
         ㄴ global                                   # 전역에서 사용되는 코드를 정의
             ㄴ ...
```

## Domain 하위 패키지

```
domain
    ㄴ challenge                                     #
    ㄴ diary                                         #
    ㄴ image                                         #
    ㄴ member                                        #
    ㄴ pet                                           #
    ㄴ stamp                                         #
```

## Global 하위 패키지

```
global
    ㄴ ...
    ㄴ exception
    |   ㄴ advice
    |   |   ㄴ GlobalExceptionHandler                #
    |   ㄴ errorCode
    |   |   ㄴ ErrorCode                             #
    |   |   ㄴ MemberCode                            #
    |   |   ㄴ DiaryErrorCode                        #
    |   |   ㄴ PetErrorCode                          #
    |   |   ㄴ ...
    |   ㄴ BusinessException                         #
    |   ㄴ ErrorResponse                             #
    ㄴ ...
```

```
global
    ㄴ ...
    ㄴ jwt
    |   ㄴ api
    |   |   ㄴ dto
    |   |   |   ㄴ AccessTokenRequest                #
    |   |   |   ㄴ AccessTokenResponse               #
    |   |   ㄴ AuthController                        #
    |   ㄴ application
    |   |   ㄴ JwtService                            #
    |   |   ㄴ RefreshTokenService                   #
    |   ㄴ dao
    |   |   ㄴ RefreshTokenDao                       #
    |   ㄴ AuthToken                                 #
    |   ㄴ JwtAuthenticationFilter                   #
    |   ㄴ TokenProvider                             #
    ㄴ ...
```

```
global
    ㄴ ...
    ㄴ oauth
    |   ㄴ handler
    |   |   ㄴ OAuth2AuthenticationFailureHandler    #
    |   |   ㄴ OAuth2AuthenticationSuccessHandler    #
    |   ㄴ provider
    |   |   |   ㄴ constants
    |   |   |      ㄴ OAuth2Provider                 #
    |   |   ㄴ KakaoUserInfo                         #
    |   |   ㄴ NaverUserInfo                         #
    |   |   ㄴ OAth2UserInfo                         #
    |   ㄴ CustomOAuth2USer                          #
    |   ㄴ PrincipalOauth2UserService                #
    ㄴ ...
```

# 3️⃣ 애플리케이션 배포

master 브랜치에 푸시했을 때, Jenkins가 Docker Image를 빌드하고 AWS EC2 위에서 구동합니다.

![image](https://github.com/dasi-bom/dasi-bom-server/assets/59405576/8945910a-81b8-4e06-b5a1-9355041add4d)

# 4️⃣ Entity Relationship Diagram

![image](https://github.com/dasi-bom/dasi-bom-server/assets/59405576/d145c162-3b47-4176-9adf-60e979ff27ec)

# 5️⃣ User Case Diagram

![image](https://github.com/dasi-bom/dasi-bom-server/assets/59405576/5b5b900b-2f2e-4c56-9533-536aec0dbbc0)

# 6️⃣ API 명세 (request/response 포함)

## 🟠 Member APIs

### 1) 사용자 프로필 갱신

```text
PUT /member/profile
Host : ${EC2_IP}:${PORT}
Authorization: Bearer ${ACCESS_TOKEN}
Content-type : application/json
```

- 소셜 로그인 단계 이후 추가적인 정보(ex. 닉네임)를 등록하여 사용자 프로필을 갱신하는 엔드포인트입니다.
- 닉네임 중복에 대한 유효성 검사를 진행합니다.

#### Request Body

```json
{
  "nickname": "밍밍"
}
```

|     Name     |  Type  | Description | Nullable | Constraint |
|:------------:|:------:|:-----------:|:--------:|:----------:|
| **nickname** | String |     닉네임     |  false   |     -      |

#### Response

```json
{
  "name": "xxx",
  "username": "kakao_xxx",
  "email": "xx@naver.com",
  "mobile": null,
  "provider": "KAKAO",
  "providerId": "xxx",
  "profileImage": null,
  "nickname": "밍밍"
}
```

### 2) 프로필 이미지 업로드

```text
POST /member/profile/images
Host : ${EC2_IP}:${PORT}
Authorization: Bearer ${ACCESS_TOKEN}
Content-type : multipart/form-data
```

- AWS S3 Bucket에 이미지를 업로드합니다.
- 데이터베이스에는 이미지 파일 자체가 아닌 경로(path)를 저장합니다.

#### Request Body

- form-data
    - Key: `multipartFile`
    - Value: ${FILE}

#### Response

```json
{
  "name": "xxx",
  "username": "kakao_xxx",
  "email": "xx@naver.com",
  "mobile": null,
  "provider": "KAKAO",
  "providerId": "xxx",
  "profileImage": "https://dasibom-app-bucket.s3.ap-northeast-2.amazonaws.com/Profile/Member/db36ed9c-806a-4f4f-8a89-f66f5317d9bc.jpeg",
  "nickname": "밍밍"
}
```

### 3) 내 프로필 조회

```text
GET /member/profile
Host : ${EC2_IP}:${PORT}
Authorization: Bearer ${ACCESS_TOKEN}
```

- 회원 프로필을 조회할 수 있는 엔드포인트입니다.
- 회원이 보호했던 동물 정보를 포함하여 회원 정보를 조회합니다.

#### Response

```json
{
  "name": "xxx",
  "username": "kakao_xxx",
  "email": "xx@naver.com",
  "mobile": null,
  "provider": "KAKAO",
  "providerId": "xxx",
  "profileImage": "https://dasibom-app-bucket.s3.ap-northeast-2.amazonaws.com/Profile/Member/db36ed9c-806a-4f4f-8a89-f66f5317d9bc.jpeg",
  "nickname": "밍밍",
  "petProfileResponses": [
    {
      "petId": 1,
      "providerId": "xxx",
      "petInfo": {
        "name": "체리",
        "age": 10,
        "type": "FOX",
        "sex": "MALE",
        "bio": "hello"
      },
      "petTempProtectedInfo": {
        "status": "IN_PROGRESS",
        "startTempProtectedDate": "2023-08-13",
        "endTempProtectedDate": null
      },
      "isSuccess": true,
      "timestamp": "2023-08-13T17:47:47.121189"
    },
    {
      "petId": 2,
      "providerId": "xxx",
      "petInfo": {
        "name": "체리",
        "age": 10,
        "type": "FOX",
        "sex": "MALE",
        "bio": "hello"
      },
      "petTempProtectedInfo": {
        "status": "IN_PROGRESS",
        "startTempProtectedDate": "2023-08-13",
        "endTempProtectedDate": null
      },
      "isSuccess": true,
      "timestamp": "2023-08-13T17:47:47.121259"
    }
  ]
}
```

## 🟠 Pet APIs

## 🟠 Diary APIs

### 1) 일기 생성

```text
POST /diary
Host : ${EC2_IP}:${PORT}
Authorization: Bearer ${ACCESS_TOKEN}
Content-type : application/json
```

- 일기 게시글을 생성할 수 있는 엔드포인트입니다.
- 이미지를 제외한 정보를 등록합니다.
- `challengeId`를 입력하면 해당하는 챌린지로 등록되고, 입력하지 않으면 일상 일기로 등록됩니다.

#### Request Body

```json
{
  "petId": 2,
  "content": "hihi",
  "challengeId": 1,
  "stamps": [
    1,
    2
  ],
  "isPublic": "True"
}
```

|      Name       |    Type    | Description | Nullable | Constraint |
|:---------------:|:----------:|:-----------:|:--------:|:----------:|
|    **petId**    |    Long    |    대상 동물    |  false   |     -      |
|   **content**   |   String   |    일기 내용    |  false   |     -      |
| **challengeId** |    Long    |     챌린지     |   true   |     -      |
|   **stamps**    | List<Long> |     스탬프     |  false   |     -      |
|  **isPublic**   |   String   |    공개 범위    |  false   |     -      |

#### Response

```json
{
  "id": 12,
  "pet": "너구리",
  "isChallenge": true,
  "challenge": "쿨쿨일기",
  "images": [],
  "author": "밍밍",
  "content": "hihi",
  "diaryStamps": [
    "산책",
    "간식"
  ],
  "isPublic": true
}
```

## 🟠 Stamp APIs

### 1) 스탬프 등록

```text
POST /stamp
Host : ${EC2_IP}:${PORT}
Authorization: Bearer ${ACCESS_TOKEN}
Content-type : application/json
```

- 스탬프를 등록할 수 있는 엔드포인트입니다.
- 관리자만 접근할 수 있습니다.

#### Request Body

```json
{
  "name": "간식"
}
```

|   Name   |  Type  | Description | Nullable | Constraint |
|:--------:|:------:|:-----------:|:--------:|:----------:|
| **name** | String |   스탬프 이름    |  false   |     -      |

#### Response

```json
{
  "name": "간식",
  "registeredBy": "xxx"
}
```

## 🟠 Challenge APIs

### 1) 챌린지 등록

```text
POST /challenge
Host : ${EC2_IP}:${PORT}
Authorization: Bearer ${ACCESS_TOKEN}
Content-type : application/json
```

- 챌린지를 등록할 수 있는 엔드포인트입니다.
- 관리자만 접근할 수 있습니다.

#### Request Body

```json
{
  "name": "날도 좋은데, 산책일기 어때요?",
  "description": "산책하는 모습을 공유하는 챌린지",
  "startDate": "2023-10-12",
  "endDate": "2023-10-30",
  "challengeType": "goal"
}
```

|       Name        |  Type  | Description | Nullable |             Constraint             |
|:-----------------:|:------:|:-----------:|:--------:|:----------------------------------:|
|     **name**      | String |   챌린지 이름    |  false   |                 -                  |
|  **description**  | String |   챌린지 설명    |  false   |                 -                  |
|   **startDate**   | String |   챌린지 시작일   |  false   |          `yyyy-mm-dd` 형식           |
|    **endDate**    | String |   챌린지 종료일   |  false   |          `yyyy-mm-dd` 형식           |
| **challengeType** | String |   챌린지 타입    |  false   | `period`/`goal`/`partnership` 중 택1 |

#### Response

```json
{
  "name": "날도 좋은데, 산책일기 어때요?",
  "challengeType": "목표 달성 챌린지",
  "registeredBy": "xxx"
}
```




