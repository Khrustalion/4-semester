package org.khrustalev.model.services;

import org.khrustalev.model.entities.owners.Owner;
import org.khrustalev.model.entities.pets.Pet;
import org.khrustalev.model.services.dto.PetDto;
import org.khrustalev.repositories.OwnerRepository;
import org.khrustalev.repositories.PetRepository;
import org.khrustalev.model.services.exceptions.EntityDoesNotExistException;
import org.khrustalev.model.services.dto.OwnerDto;
import org.khrustalev.model.services.dto.OwnerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OwnerService {
    private PetRepository petRepository;
    private OwnerRepository ownerRepository;
    private OwnerMapper ownerMapper;

    @Autowired
    public OwnerService(PetRepository petRepository,
                        OwnerRepository ownerRepository,
                        OwnerMapper ownerMapper) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
    }

    public OwnerDto save(OwnerDto ownerDto) {
        Owner owner = this.ownerRepository.save(this.ownerMapper.dto2Entity(ownerDto));


        return this.ownerMapper.entity2Dto(owner);
    }

    public OwnerDto update(OwnerDto ownerDto) {
        if (this.ownerRepository.findById(ownerDto.getId()).isEmpty()) {
            throw new EntityDoesNotExistException("Owner with id " + ownerDto.getId() + " does not exist");
        }

        Owner owner = this.ownerMapper.dto2Entity(ownerDto);

        return this.ownerMapper.entity2Dto(this.ownerRepository.save(owner));
    }

    public OwnerDto findById(long id) {
        Owner owner = this.ownerRepository.findById(id).orElse(null);

        if (owner == null) {
            throw new EntityDoesNotExistException("Owner with id" + id + " not found");
        }

        return this.ownerMapper.entity2Dto(owner);
    }

    public List<OwnerDto> findAll() {
        Iterable<Owner> ownersEntity = this.ownerRepository.findAll();
        List<OwnerDto> ownerDto = new ArrayList<>();

        ownersEntity.forEach(owner -> ownerDto.add(this.ownerMapper.entity2Dto(owner)));

        return ownerDto;
    }

    public Page<OwnerDto> getOwnersBySortingPaging(int page, int size, String sortBy, boolean descending) {
        Sort sort = descending ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Owner> petsPage = this.ownerRepository.findAll(pageable);

        return petsPage.map(this.ownerMapper::entity2Dto);
    }

    public OwnerDto deleteById(long id) {
        Owner owner = this.ownerRepository.findById(id).orElse(null);

        if (owner == null) {
            throw new EntityDoesNotExistException("Owner with id " + id + " does not exist");
        }

        OwnerDto ownerDto = this.ownerMapper.entity2Dto(owner);

        this.ownerRepository.delete(owner);

        return ownerDto;
    }

    public OwnerDto addPet(Long ownerId, Long petId) {
        Owner owner = this.ownerRepository.findById(ownerId).orElse(null);

        if (owner == null) {
            throw new EntityDoesNotExistException("Owner with id " + ownerId + " does not exist");
        }

        Pet pet = this.petRepository.findById(petId).orElse(null);

        if (pet == null) {
            throw new EntityDoesNotExistException("Pet with id " + petId + " does not exist");
        }

        pet.setOwner(owner);

        owner.addPet(pet);

        this.ownerRepository.save(owner);

        return this.ownerMapper.entity2Dto(owner);
    }
}
