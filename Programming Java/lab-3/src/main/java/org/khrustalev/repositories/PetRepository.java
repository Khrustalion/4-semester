package org.khrustalev.repositories;

import org.khrustalev.model.entities.pets.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {}
