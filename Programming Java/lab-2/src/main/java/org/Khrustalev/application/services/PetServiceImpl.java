package org.Khrustalev.application.services;

import org.Khrustalev.Presentation.dto.OwnerMapping;
import org.Khrustalev.Presentation.dto.PetMapping;
import org.Khrustalev.Presentation.dto.PetRequestDto;
import org.Khrustalev.application.contracts.services.PetService;
import org.Khrustalev.application.exceptions.DoesNotExsistExcpetion;
import org.Khrustalev.domain.Entities.Owners.Owner;
import org.Khrustalev.domain.Entities.Pets.Pet;
import org.Khrustalev.application.contracts.dao.Dao;

import java.util.List;

public class PetServiceImpl implements PetService {
    private final Dao<Owner> ownerDao;
    private final Dao<Pet> petsDao;
    private final OwnerMapping ownerMapping;
    private final PetMapping petMapping;

    public PetServiceImpl(Dao<Owner> ownerDao,
                          Dao<Pet> petsDao,
                          OwnerMapping ownerMapping,
                          PetMapping petMapping) {
        this.ownerDao = ownerDao;
        this.petsDao = petsDao;
        this.ownerMapping = ownerMapping;
        this.petMapping = petMapping;
    }

    @Override
    public PetRequestDto add(PetRequestDto pet) {
        Pet petEntity = petsDao.save(this.petMapping.dto2Entity(pet));

        return petMapping.entity2Dto(petEntity);
    }

    @Override
    public PetRequestDto getById(Long id) {
        Pet petEntity = petsDao.getById(id);

        if (petEntity == null) {
            throw new DoesNotExsistExcpetion("Pet not found");
        }

        return petMapping.entity2Dto(petEntity);
    }

    @Override
    public void deleteById(Long id) {
        petsDao.deleteById(id);
    }

    @Override
    public void deleteAll() {
        petsDao.deleteAll();
    }

    @Override
    public PetRequestDto addFriend(long petId, long friendId) throws DoesNotExsistExcpetion {
        Pet pet = petsDao.getById(petId);

        if (pet == null) {
            throw new DoesNotExsistExcpetion("Pet does not exist");
        }

        Pet friendEntity = petsDao.getById(friendId);

        if (friendEntity == null) {
            throw new DoesNotExsistExcpetion("Friend does not exist");
        }

        pet.addFriend(friendEntity);

        Pet petEntity = petsDao.save(pet);

        return petMapping.entity2Dto(petEntity);
    }

    @Override
    public PetRequestDto removeFriend(long petId, long friendId) throws DoesNotExsistExcpetion {
        Pet pet = petsDao.getById(petId);

        if (pet == null) {
            throw new DoesNotExsistExcpetion("Pet does not exist");
        }

        Pet friendEntity = petsDao.getById(friendId);

        if (friendEntity == null) {
            throw new DoesNotExsistExcpetion("Friend does not exist");
        }

        pet.removeFriend(friendEntity);

        Pet petEntity = petsDao.save(pet);

        return petMapping.entity2Dto(petEntity);
    }

    @Override
    public PetRequestDto changeOwner(long petId, long oldOwnerId, long newOwnerId) throws DoesNotExsistExcpetion {
        Pet pet = petsDao.getById(petId);

        if (pet == null) {
            throw new DoesNotExsistExcpetion("Pet does not exist");
        }

        Owner oldOwner = ownerDao.getById(oldOwnerId);

        if (oldOwner == null) {
            throw new DoesNotExsistExcpetion("Old owner does not exist");
        }

        Owner newOwner = ownerDao.getById(newOwnerId);

        if (newOwner == null) {
            throw new DoesNotExsistExcpetion("New owner does not exist");
        }

        pet.setOwner(newOwner);

        List<Pet> newPets = newOwner.getPets();
        newPets.add(pet);
        newOwner.setPets(newPets);

        List<Pet> oldPets = oldOwner.getPets();
        oldPets.remove(pet);
        oldOwner.setPets(oldPets);

        Pet petEntity = petsDao.save(pet);
        ownerDao.save(oldOwner);
        ownerDao.save(newOwner);

        return petMapping.entity2Dto(petEntity);
    }

    @Override
    public List<PetRequestDto> getAll() {
        return petsDao.getAll()
                .stream()
                .map(petMapping::entity2Dto)
                .toList();
    }
}
