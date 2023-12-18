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
@Transactional
public interface PetRepository extends JpaRepository<Pet, Long> {

	@Query("SELECT p.id FROM Pet p WHERE p.owner =:owner")
	List<Long> findPetIdsByOwnerId(Member owner);

	Optional<Pet> findPetById(Long petId);

	Optional<Pet> findPetByIdAndOwner(Long petId, Member owner);

	@Query("SELECT MAX(p.id) FROM Pet p")
	Optional<Long> getLastId();
}
