package com.example.server.domain.image.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.image.model.Image;

@Repository
@Transactional
public interface ImageRepository extends JpaRepository<Image, Long> {
}


