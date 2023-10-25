package com.example.server.domain.challenge.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.challenge.model.Challenge;

@Repository
@Transactional
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
