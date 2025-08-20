package org.khrustalev.model.services.dto;

import org.khrustalev.model.entities.owners.Owner;
import org.khrustalev.model.entities.pets.Pet;
import org.khrustalev.repositories.OwnerRepository;
import org.khrustalev.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OwnerMapper {
    private PetRepository petRepository;
    private OwnerRepository ownerRepository;

    @Autowired
    public OwnerMapper(PetRepository petRepository, OwnerRepository ownerRepository) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
    }

    public Owner dto2Entity(OwnerDto dto) {
        List<Pet> pets = dto.getPets().stream()
                .map(petRepository::findById)
                .flatMap(Optional::stream)
                .toList();

        Owner owner = new Owner();

        owner.setId(dto.getId());
        owner.setBirthday(dto.getBirthday());
        owner.setName(dto.getName());
        owner.setPets(pets);

        return owner;
    }

    public OwnerDto entity2Dto(Owner entity) {
        return new OwnerDto(
                entity.getId(),
                entity.getName(),
                entity.getBirthday(),
                entity.getPets().stream().map(Pet::getId).toList()
        );
    }
}
