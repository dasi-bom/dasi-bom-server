package com.example.server.domain.image.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.image.model.Image;

@Repository
@Transactional(readOnly = true)
public interface ImageQueryRepository extends JpaRepository<Image, Long> {
}


