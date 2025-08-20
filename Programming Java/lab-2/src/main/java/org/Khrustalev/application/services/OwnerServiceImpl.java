package org.Khrustalev.application.services;

import org.Khrustalev.Presentation.dto.OwnerMapping;
import org.Khrustalev.Presentation.dto.OwnerRequestDto;
import org.Khrustalev.Presentation.dto.PetMapping;
import org.Khrustalev.Presentation.dto.PetRequestDto;
import org.Khrustalev.application.contracts.services.OwnerService;
import org.Khrustalev.application.exceptions.DoesNotExsistExcpetion;
import org.Khrustalev.domain.Entities.Owners.Owner;
import org.Khrustalev.domain.Entities.Pets.Pet;
import org.Khrustalev.application.contracts.dao.Dao;

import java.util.List;

public class OwnerServiceImpl implements OwnerService {
    private final Dao<Owner> ownerDao;
    private final Dao<Pet> petsDao;
    private final OwnerMapping ownerMapping;
    private final PetMapping petMapping;

    public OwnerServiceImpl(Dao<Owner> ownerDao,
                            Dao<Pet> petsDao,
                            OwnerMapping ownerMapping,
                            PetMapping petMapping) {
        this.ownerDao = ownerDao;
        this.petsDao = petsDao;
        this.ownerMapping = ownerMapping;
        this.petMapping = petMapping;
    }

    @Override
    public OwnerRequestDto add(OwnerRequestDto owner) {
        Owner ownerEntity = ownerDao.save(this.ownerMapping.dto2Entity(owner));

        return ownerMapping.entity2Dto(ownerEntity);
    }

    @Override
    public OwnerRequestDto getById(Long id) {
        Owner ownerEntity = ownerDao.getById(id);

        if (ownerEntity == null) {
            throw new DoesNotExsistExcpetion("Owner not found");
        }

        return ownerMapping.entity2Dto(ownerEntity);
    }

    @Override
    public void deleteById(Long id) {
        ownerDao.deleteById(id);
    }

    @Override
    public void deleteAll() {
        ownerDao.deleteAll();
    }

    @Override
    public OwnerRequestDto addPet(long ownerId, long petId) throws DoesNotExsistExcpetion {
        Owner owner = ownerDao.getById(ownerId);

        if (owner == null) {
            throw new DoesNotExsistExcpetion("Owner not found");
        }

        Pet pet = petsDao.getById(petId);

        if (pet == null) {
            throw new DoesNotExsistExcpetion("Pet not found");
        }

        owner.addPet(pet);

        Owner ownerEntity = ownerDao.update(owner);

        return ownerMapping.entity2Dto(ownerEntity);
    }

    @Override
    public List<OwnerRequestDto> getAll() {
        return ownerDao.getAll()
                .stream()
                .map(ownerMapping::entity2Dto)
                .toList();
    }

    @Override
    public OwnerRequestDto RemovePet(long ownerId, PetRequestDto pet) throws DoesNotExsistExcpetion {
        Owner owner = ownerDao.getById(ownerId);

        if (owner == null) {
            throw new DoesNotExsistExcpetion("Owner not found");
        }

        owner.removePet(petMapping.dto2Entity(pet));

        Owner ownerEntity = ownerDao.save(owner);

        return ownerMapping.entity2Dto(ownerEntity);
    }
}
