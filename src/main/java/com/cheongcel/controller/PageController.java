package com.cheongcel.controller;

import com.cheongcel.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final ProjectService projectService;

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("projects", projectService.getAllProjects());
        return "index";
    }

    @GetMapping("/archive")
    public String archive(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("projects", projectService.getAllProjects());
        return "archive/index";
    }

    @GetMapping("/archive/{id}")
    public String archiveDetail(@PathVariable Long id, Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("project", projectService.getProject(id));
        return "archive/detail";
    }

    @GetMapping("/about")
    public String about(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "about/index";
    }

    @GetMapping("/contact")
    public String contact(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "contact/index";
    }
}
