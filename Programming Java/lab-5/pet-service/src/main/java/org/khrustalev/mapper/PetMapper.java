package org.khrustalev.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.khrustalev.dto.PetDto;
import org.khrustalev.entity.Pet;
import org.khrustalev.exception.PetNotFoundException;
import org.springframework.stereotype.Component;
import org.khrustalev.repository.PetRepository;

@Component
public class PetMapper {

    private final PetRepository petRepository;

    public PetMapper(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public PetDto toDto(Pet pet) {
        List<Long> friendIds = pet.getFriends() != null
                ? pet.getFriends().stream()
                .map(Pet::getId)
                .collect(Collectors.toList())
                : Collections.emptyList();

        PetDto dto = new PetDto();
        dto.setId(pet.getId());
        dto.setNickname(pet.getNickname());
        dto.setBirthday(pet.getBirthday());
        dto.setBreed(pet.getBreed());
        dto.setColor(pet.getColor());
        dto.setOwnerId(pet.getOwnerId());
        dto.setFriends(friendIds);
        return dto;
    }

    public Pet toEntity(PetDto dto) {
        Pet pet = new Pet();
        pet.setId(dto.getId());
        pet.setNickname(dto.getNickname());
        pet.setBirthday(dto.getBirthday());
        pet.setBreed(dto.getBreed());
        pet.setColor(dto.getColor());
        pet.setOwnerId(dto.getOwnerId());

        List<Pet> friends = dto.getFriends() != null
                ? dto.getFriends().stream()
                .map(id -> petRepository.findById(id)
                        .orElseThrow(() -> new PetNotFoundException("Friend pet not found: " + id)))
                .collect(Collectors.toList())
                : Collections.emptyList();

        pet.setFriends(friends);
        return pet;
    }
}
