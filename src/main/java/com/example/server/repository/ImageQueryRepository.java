package com.example.server.repository;

import com.example.server.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ImageQueryRepository extends JpaRepository<Image, Long> {
}


