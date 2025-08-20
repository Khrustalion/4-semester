package org.Khrustalev.Presentation.dto;

import org.Khrustalev.domain.Entities.Owners.Owner;
import org.Khrustalev.domain.Entities.Pets.Pet;
import org.Khrustalev.application.contracts.dao.Dao;

import java.util.List;

public class PetMapping {
    private final Dao<Pet> petDao;
    private final Dao<Owner> ownerDao;

    public PetMapping(Dao<Pet> petDao, Dao<Owner> ownerDao) {
        this.petDao = petDao;
        this.ownerDao = ownerDao;
    }

    public Pet dto2Entity(PetRequestDto dto) {
        List<Pet> pets = dto.getFriends()
                .stream()
                .map(this.petDao::getById)
                .toList();

        Owner owner = ownerDao.getById(dto.getOwnerId());

        if (owner == null) {
            return null;
        }

        Pet pet = new Pet();

        pet.setId(dto.getId());
        pet.setNickname(dto.getnickname());
        pet.setBirthday(dto.getBirthday());
        pet.setBreed(dto.getBreed());
        pet.setOwner(owner);
        pet.setFriends(pets);

        return pet;
    }

    public PetRequestDto entity2Dto(Pet pet) {
        return new PetRequestDto(
                pet.getId(),
                pet.getNickname(),
                pet.getBirthday(),
                pet.getBreed(),
                pet.getOwner().getId(),
                pet.getFriends().stream().map(Pet::getId).toList());
    }
}
