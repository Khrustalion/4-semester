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
public class PetMapper {
    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    @Autowired
    public PetMapper(PetRepository petRepository, OwnerRepository ownerRepository) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
    }

    public Pet dto2Entity(PetDto dto) {
        List<Pet> pets = dto.getFriends().stream()
                .map(this.petRepository::findById)
                .flatMap(Optional::stream)
                .toList();

        Owner owner = ownerRepository.findById(dto.getOwnerId()).orElse(null);

        if (owner == null) {
            return null;
        }

        Pet pet = new Pet();

        pet.setId(dto.getId());
        pet.setNickname(dto.getNickname());
        pet.setBirthday(dto.getBirthday());
        pet.setBreed(dto.getBreed());
        pet.setOwner(owner);
        pet.setFriends(pets);

        return pet;
    }

    public PetDto entity2Dto(Pet pet) {
        return new PetDto(
                pet.getId(),
                pet.getNickname(),
                pet.getBirthday(),
                pet.getBreed(),
                pet.getColor(),
                pet.getOwner().getId(),
                pet.getFriends().stream().map(Pet::getId).toList()
        );
    }
}
