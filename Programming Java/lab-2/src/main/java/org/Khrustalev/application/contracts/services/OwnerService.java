package org.Khrustalev.application.contracts.services;

import org.Khrustalev.Presentation.dto.OwnerRequestDto;
import org.Khrustalev.Presentation.dto.PetRequestDto;
import org.Khrustalev.application.exceptions.DoesNotExsistExcpetion;

import java.util.List;


public interface OwnerService {
    public OwnerRequestDto add(OwnerRequestDto owner);

    public OwnerRequestDto getById(Long id) throws DoesNotExsistExcpetion;

    public void deleteById(Long id);

    public void deleteAll();

    public OwnerRequestDto addPet(long ownerId, long petId) throws DoesNotExsistExcpetion;

    public List<OwnerRequestDto> getAll();

    public OwnerRequestDto RemovePet(long ownerId, PetRequestDto pet) throws DoesNotExsistExcpetion;
}
