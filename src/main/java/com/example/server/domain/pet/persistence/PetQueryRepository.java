package com.example.server.domain.pet.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.model.Pet;

@Repository
@Transactional(readOnly = true)
public interface PetQueryRepository extends JpaRepository<Pet, Long> {
	Optional<Pet> findById(Long id);

	@Query("SELECT p.id FROM Pet p WHERE p.owner =:owner")
	List<Long> findPetIdsByOwnerId(Member owner);
}
