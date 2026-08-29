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