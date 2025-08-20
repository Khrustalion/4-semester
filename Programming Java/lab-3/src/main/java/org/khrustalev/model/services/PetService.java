package org.khrustalev.model.services;

import org.khrustalev.model.entities.pets.Pet;
import org.khrustalev.repositories.OwnerRepository;
import org.khrustalev.repositories.PetRepository;
import org.khrustalev.model.services.exceptions.EntityDoesNotExistException;
import org.khrustalev.model.services.dto.PetDto;
import org.khrustalev.model.services.dto.PetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PetService {
    private PetRepository petRepository;
    private OwnerRepository ownerRepository;
    private PetMapper petMapper;

    @Autowired
    private PetService(PetRepository petRepository,
                       OwnerRepository ownerRepository,
                       PetMapper petMapper) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
        this.petMapper = petMapper;
    }

    public PetDto save(PetDto petDto) {
        Pet pet = this.petRepository.save(petMapper.dto2Entity(petDto));

        return this.petMapper.entity2Dto(pet);
    }

    public PetDto update(PetDto petDto) {
        if (this.petRepository.findById(petDto.getId()).isEmpty()) {
            throw new EntityDoesNotExistException("Pet with id " + petDto.getId() + " does not exist");
        }

        Pet pet = this.petMapper.dto2Entity(petDto);

        return petMapper.entity2Dto(this.petRepository.save(pet));
    }

    public PetDto findById(long id) {
        Pet pet = this.petRepository.findById(id).orElse(null);

        if (pet == null) {
            throw new EntityDoesNotExistException("Pet with id " + id + " does not exist");
        }

        return this.petMapper.entity2Dto(pet);
    }

    public PetDto deleteById(long id) {
        Pet pet = this.petRepository.findById(id).orElse(null);

        if (pet == null) {
            throw new EntityDoesNotExistException("Pet with id " + id + " does not exist");
        }

        this.petRepository.delete(pet);

        return this.petMapper.entity2Dto(pet);
    }

    public PetDto addFriend(long petId, long friendId) {
        Pet pet = this.petRepository.findById(petId).orElse(null);

        if (pet == null) {
            throw new EntityDoesNotExistException("Pet with id " + petId + " does not exist");
        }

        Pet friend = this.petRepository.findById(friendId).orElse(null);

        if (friend == null) {
            throw new EntityDoesNotExistException("Friend with id " + friendId + " does not exist");
        }

        pet.addFriend(friend);
        friend.addFriend(pet);

        petRepository.save(pet);

        return this.petMapper.entity2Dto(pet);
    }

    public List<PetDto> findAll() {
        Iterable<Pet> petsEntity = this.petRepository.findAll();

        List<PetDto> petDto = new ArrayList<>();
        petsEntity.forEach(pet -> petDto.add(this.petMapper.entity2Dto(pet)));

        return petDto;
    }

    public Page<PetDto> getPetsBySortingPaging(int page, int size, String sortBy, boolean descending) {
        Sort sort = descending ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Pet> petsPage = petRepository.findAll(pageable);

        return petsPage.map(this.petMapper::entity2Dto);
    }
}
