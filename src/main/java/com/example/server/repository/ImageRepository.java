package com.example.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}


