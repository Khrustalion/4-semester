package org.Khrustalev.Presentation.dto;

import org.Khrustalev.domain.Entities.Owners.Owner;
import org.Khrustalev.domain.Entities.Pets.Pet;
import org.Khrustalev.application.contracts.dao.Dao;

import java.util.List;

public class OwnerMapping {
    private final Dao<Pet> petDao;
    private final Dao<Owner> ownerDao;

    public OwnerMapping(Dao<Pet> petDao, Dao<Owner> ownerDao) {
        this.petDao = petDao;
        this.ownerDao = ownerDao;
    }

    public Owner dto2Entity(OwnerRequestDto dto) {
        List<Pet> pets = dto.getPets().stream().map(petDao::getById).toList();

        Owner owner = new Owner();

        owner.setId(dto.getId());
        owner.setBirthday(dto.getBirthday());
        owner.setName(dto.getName());
        owner.setPets(pets);

        return owner;
    }

    public OwnerRequestDto entity2Dto(Owner entity) {
        return new OwnerRequestDto(
                entity.getId(),
                entity.getName(),
                entity.getBirthday(),
                entity.getPets().stream().map(Pet::getId).toList()
        );
    }
}
