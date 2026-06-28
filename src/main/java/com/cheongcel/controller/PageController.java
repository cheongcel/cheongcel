package com.cheongcel.controller;

import com.cheongcel.service.ArtworkService;
import com.cheongcel.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final ArtworkService artworkService;
    private final ProductService productService;

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("recentArtworks", artworkService.getRecentArtworks(3));
        model.addAttribute("featuredProducts", productService.getFeaturedProducts(4));
        return "index";
    }

    @GetMapping("/gallery")
    public String gallery(@RequestParam(required = false) String category, Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("artworks", artworkService.getArtworks(category));
        model.addAttribute("selectedCategory", category);
        return "gallery/index";
    }

    @GetMapping("/gallery/{id}")
    public String artworkDetail(@PathVariable Long id, Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("artwork", artworkService.getArtwork(id));
        return "gallery/detail";
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(required = false) String category, Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("products", productService.getProducts(category));
        model.addAttribute("selectedCategory", category);
        return "shop/index";
    }

    @GetMapping("/shop/{id}")
    public String productDetail(@PathVariable Long id, Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("product", productService.getProduct(id));
        return "shop/detail";
    }

    @GetMapping("/project")
    public String project(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "project/index";
    }

    @GetMapping("/about")
    public String about(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "about/index";
    }
}