package com.example.server.persistence.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.image.Image;

@Repository
@Transactional(readOnly = true)
public interface ImageQueryRepository extends JpaRepository<Image, Long> {
}


