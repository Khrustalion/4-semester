package org.Khrustalev.Presentation.controller;

import org.Khrustalev.Presentation.dto.PetRequestDto;
import org.Khrustalev.application.contracts.services.PetService;
import org.Khrustalev.application.exceptions.DoesNotExsistExcpetion;

import java.util.List;

public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    public PetRequestDto add(PetRequestDto pet) {
        return petService.add(pet);
    }

    public PetRequestDto getById(Long id)  {
        return petService.getById(id);
    }


    public void deleteById(Long id) {
        petService.deleteById(id);
    }

    public void deleteAll() {
        petService.deleteAll();
    }

    public PetRequestDto addFriend(long petId, long friendId) {
        return petService.addFriend(petId, friendId);
    }

    public PetRequestDto removeFriend(long petId, long friendId) {
        return petService.removeFriend(petId, friendId);
    }

    public PetRequestDto changeOwner(long petId, long oldOwnerId, long newOwnerId) {
        return petService.changeOwner(petId, oldOwnerId, newOwnerId);
    }

    public List<PetRequestDto> getAll() {
        return petService.getAll();
    }
}
