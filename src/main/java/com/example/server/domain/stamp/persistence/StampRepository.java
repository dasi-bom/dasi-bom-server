package com.example.server.domain.stamp.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.stamp.model.Stamp;
import com.example.server.domain.stamp.model.constants.StampType;

@Repository
@Transactional
public interface StampRepository extends JpaRepository<Stamp, Long> {
	boolean existsByStampType(StampType stampType);

	Optional<Stamp> findByStampType(StampType stampType);
}
