package com.example.server.domain.stamp.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.stamp.model.Stamp;

@Repository
@Transactional
public interface StampRepository extends JpaRepository<Stamp, Long> {
}


