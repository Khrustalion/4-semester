package org.khrustalev.service;

import java.util.List;

import lombok.AllArgsConstructor;
import org.khrustalev.dto.Page.PageResponseDto;
import org.khrustalev.dto.Page.PageResponseMapper;
import org.khrustalev.dto.PetDto;
import org.khrustalev.entity.Pet;
import org.khrustalev.exception.PetNotFoundException;
import org.khrustalev.mapper.PetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.khrustalev.repository.PetRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PetService {
    private PetRepository petRepository;
    private PetMapper petMapper;

    @Autowired
    public PetService(PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
    }

    public PetDto save(PetDto dto) {
        Pet pet = petMapper.toEntity(dto);
        pet.setId(null);
        Pet saved = petRepository.save(pet);
        return petMapper.toDto(saved);
    }

    public PetDto update(PetDto dto) {
        Long id = dto.getId();
        if (!petRepository.existsById(id)) {
            throw new PetNotFoundException("Pet with id " + id + " does not exist");
        }
        Pet pet = petMapper.toEntity(dto);
        Pet updated = petRepository.save(pet);
        return petMapper.toDto(updated);
    }

    @Transactional(readOnly = true)
    public PetDto findById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet with id " + id + " not found"));
        return petMapper.toDto(pet);
    }

    public PetDto deleteById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet with id " + id + " not found"));
        petRepository.delete(pet);
        return petMapper.toDto(pet);
    }

    public List<PetDto> findAll() {
        return petRepository.findAll().stream()
                .map(petMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponseDto<PetDto> findAll(Pageable pageable) {
        Page<PetDto> page = petRepository.findAll(pageable)
                .map(petMapper::toDto);

        return PageResponseMapper.map(page);
    }

    @Transactional
    public PetDto addFriend(Long petId, Long friendId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet with id " + petId + " not found"));
        Pet friend = petRepository.findById(friendId)
                .orElseThrow(() -> new PetNotFoundException("Friend pet with id " + friendId + " not found"));

        pet.addFriend(friend);
        friend.addFriend(pet);

        petRepository.save(pet);

        return petMapper.toDto(pet);
    }
}
