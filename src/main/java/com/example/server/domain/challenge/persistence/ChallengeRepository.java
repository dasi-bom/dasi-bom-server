package com.example.server.domain.challenge.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.challenge.model.Challenge;
import com.example.server.domain.challenge.model.ChallengeType;

@Repository
@Transactional
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

	boolean existsByChallengeType(ChallengeType challengeType);

	Optional<Challenge> findByChallengeType(ChallengeType challengeType);
}
