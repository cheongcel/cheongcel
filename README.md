# cheongcel

개인 허브 브랜드 사이트 — 갤러리, 샵, 프로젝트, 소개를 한 곳에.

## 기술 스택

- Java 17
- Spring Boot 3.2
- Spring Data JPA
- Spring Security
- Thymeleaf + Layout Dialect
- PostgreSQL (prod) / H2 (local)
- Docker + Render

## 프로젝트 구조

```
src/main/java/com/cheongcel/
├── CheongcelApplication.java
├── controller/
│   └── PageController.java       # 모든 페이지 라우팅
├── service/
│   ├── ArtworkService.java       # 갤러리 비즈니스 로직
│   └── ProductService.java       # 샵 비즈니스 로직
├── repository/
│   ├── ArtworkRepository.java
│   ├── ProductRepository.java
│   └── OrderRepository.java
├── domain/
│   ├── Artwork.java              # 그림 작품 엔티티
│   ├── Product.java              # 굿즈 상품 엔티티
│   ├── Order.java                # 주문 엔티티 (결제 연동 준비)
│   └── OrderItem.java
└── config/
    ├── SecurityConfig.java
    └── DataInitializer.java      # 로컬 샘플 데이터

src/main/resources/
├── templates/
│   ├── layout.html               # 공통 레이아웃
│   ├── index.html                # 메인 페이지
│   ├── gallery/index.html
│   ├── shop/index.html
│   ├── project/index.html
│   └── about/index.html
└── static/
    ├── css/main.css
    └── js/main.js
```

## 실행 방법

```bash
# 로컬 (H2 인메모리 DB)
./gradlew bootRun --args='--spring.profiles.active=local'

# http://localhost:8080 접속
# H2 콘솔: http://localhost:8080/h2-console
```

## 다음 단계

### Phase 1 (지금) ✅
- [x] 기본 페이지 구조 (메인, 갤러리, 샵, 프로젝트, 소개)
- [x] DB 엔티티 설계 (Artwork, Product, Order)
- [x] 로컬/프로덕션 프로파일 분리

### Phase 2 (콘텐츠)
- [ ] 이미지 업로드 기능 (AWS S3 or Cloudinary)
- [ ] 관리자 페이지 (작품/상품 CRUD)
- [ ] 갤러리 상세 페이지
- [ ] 상품 상세 페이지

### Phase 3 (결제)
- [ ] 장바구니 기능 (세션 기반)
- [ ] 토스페이먼츠 결제 연동
- [ ] 주문 관리
- [ ] 이메일 주문 확인 (Gmail SMTP - DIVY에서 경험 있음)

### Phase 4 (배포)
- [ ] Docker 이미지 빌드
- [ ] Render 배포 (DIVY, StockPulse와 동일한 방식)
- [ ] 도메인 연결
