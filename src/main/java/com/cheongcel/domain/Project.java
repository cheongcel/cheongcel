package com.cheongcel.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private String techStack;

    private String liveUrl;
    private String githubUrl;

    private String posterUrl;

    /** 작품 소개 글 (상세 페이지 하단, 줄바꿈 가능) */
    @Column(columnDefinition = "TEXT")
    private String story;

    /** 사용 사진 URL 목록. 콤마(,)로 구분, "주소|설명" 형태로 캡션도 가능.
     *  예) /images/shots/divy-1.png|대시보드 화면,/images/shots/divy-2.png */
    @Column(columnDefinition = "TEXT")
    private String shots;

    private String coinImageUrl;

    private String coinBgColor;

    @Column(nullable = false)
    private LocalDate projectDate;

    @Column(nullable = false)
    private boolean published = true;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}