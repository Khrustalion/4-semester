package org.Khrustalev.Presentation.controller;

import org.Khrustalev.Presentation.dto.OwnerRequestDto;
import org.Khrustalev.Presentation.dto.PetRequestDto;
import org.Khrustalev.application.contracts.services.OwnerService;
import org.Khrustalev.application.exceptions.DoesNotExsistExcpetion;

import java.util.List;

public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    public OwnerRequestDto add(OwnerRequestDto owner) {
        return ownerService.add(owner);
    }

    public OwnerRequestDto getById(Long id) {
        return ownerService.getById(id);
    }

    public void deleteById(Long id) {
        ownerService.deleteById(id);
    }

    public void deleteAll() {
        ownerService.deleteAll();
    }

    public OwnerRequestDto addPet(long ownerId, long petId) {
        return ownerService.addPet(ownerId, petId);
    }

    public List<OwnerRequestDto> getAll() {
        return ownerService.getAll();
    }

    public OwnerRequestDto RemovePet(long ownerId, PetRequestDto pet) {
        return ownerService.RemovePet(ownerId, pet);
    }
}
