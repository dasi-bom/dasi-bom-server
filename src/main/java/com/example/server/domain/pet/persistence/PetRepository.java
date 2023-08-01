package com.example.server.domain.pet.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.pet.model.Pet;

@Repository
@Transactional
public interface PetRepository extends JpaRepository<Pet, Long> {
}
