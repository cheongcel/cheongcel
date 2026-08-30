package com.cheongcel.config;

import com.cheongcel.domain.Project;
import com.cheongcel.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final ProjectRepository projectRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (projectRepository.count() == 0) {
            projectRepository.save(Project.builder()
                    .title("cheongcel")
                    .description("개인 브랜드 허브 사이트")
                    .techStack("Spring Boot, Thymeleaf, PostgreSQL, Docker")
                    .liveUrl("https://cheongcel.yoossi.dev")
                    .githubUrl("https://github.com/cheongcel/cheongcel")
                    .posterUrl("/images/archive-cheongcel.png")
                    .projectDate(LocalDate.of(2025, 8, 1))
                    .published(true)
                    .build());

            projectRepository.save(Project.builder()
                    .title("DIVY")
                    .description("주식 배당금 포트폴리오 대시보드")
                    .techStack("Spring Boot, PostgreSQL, Chart.js")
                    .liveUrl("https://divy-m1ta.onrender.com")
                    .projectDate(LocalDate.of(2025, 6, 1))
                    .published(true)
                    .build());

            projectRepository.save(Project.builder()
                    .title("StockPulse")
                    .description("AI 키워드 뉴스 감성 분석 서비스")
                    .techStack("Spring Boot, Gemini API, Spring Scheduler")
                    .liveUrl("https://stockpulse-dcw8.onrender.com")
                    .projectDate(LocalDate.of(2025, 4, 1))
                    .published(true)
                    .build());
            return;
        }

        // 이미 데이터가 있으면, cheongcel 프로젝트를 포스터 이미지 카드로 전환
        projectRepository.findAll().stream()
                .filter(p -> "cheongcel".equals(p.getTitle()))
                .findFirst()
                .ifPresent(p -> {
                    p.setCoinImageUrl(null);
                    p.setCoinBgColor(null);
                    p.setPosterUrl("/images/archive-cheongcel.png");
                    projectRepository.save(p);
                });
    }
}