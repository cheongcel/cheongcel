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

    // "Spring Boot, PostgreSQL" 같은 콤마구분 텍스트
    private String techStack;

    private String liveUrl;
    private String githubUrl;

    // 포스터 이미지 URL (직접 제작한 카드 썸네일)
    private String posterUrl;

    // Archive에 표시할 날짜 (직접 지정 가능하게)
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
