package com.example.server.repository.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.pet.Pet;

@Repository
@Transactional(readOnly = true)
public interface PetQueryRepository extends JpaRepository<Pet, Long> {
}
