package com.cheongcel.config;

import com.cheongcel.domain.Artwork;
import com.cheongcel.domain.Product;
import com.cheongcel.repository.ArtworkRepository;
import com.cheongcel.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("local")
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final ArtworkRepository artworkRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (artworkRepository.count() > 0) return;

        // 샘플 작품
        artworkRepository.save(Artwork.builder()
                .title("작품 #001").category("illustration")
                .imageUrl("/images/placeholder.png").published(true).build());
        artworkRepository.save(Artwork.builder()
                .title("작품 #002").category("character")
                .imageUrl("/images/placeholder.png").published(true).build());
        artworkRepository.save(Artwork.builder()
                .title("작품 #003").category("digital")
                .imageUrl("/images/placeholder.png").published(true).build());

        // 샘플 굿즈
        productRepository.save(Product.builder()
                .name("아트 티셔츠").category("tshirt")
                .price(new BigDecimal("29000")).stock(10)
                .imageUrl("/images/placeholder.png").published(true).build());
        productRepository.save(Product.builder()
                .name("포스터 프린트").category("poster")
                .price(new BigDecimal("15000")).stock(20)
                .imageUrl("/images/placeholder.png").published(true).build());
        productRepository.save(Product.builder()
                .name("에코백").category("bag")
                .price(new BigDecimal("19000")).stock(15)
                .imageUrl("/images/placeholder.png").published(true).build());
        productRepository.save(Product.builder()
                .name("아트 노트").category("note")
                .price(new BigDecimal("12000")).stock(30)
                .imageUrl("/images/placeholder.png").published(true).build());
    }
}
