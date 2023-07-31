package com.example.server.repository.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.image.Image;

@Repository
@Transactional
public interface ImageRepository extends JpaRepository<Image, Long> {
}


