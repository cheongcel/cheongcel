package com.cheongcel.repository;

import com.cheongcel.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByPublishedTrueOrderByProjectDateDesc();
}
