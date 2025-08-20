package org.khrustalev.repositories;

import org.khrustalev.model.entities.owners.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {}