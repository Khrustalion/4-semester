package org.Khrustalev.application.contracts.services;

import org.Khrustalev.Presentation.dto.PetRequestDto;
import org.Khrustalev.application.exceptions.DoesNotExsistExcpetion;

import java.util.List;

public interface PetService {
    public PetRequestDto add(PetRequestDto pet);

    public PetRequestDto getById(Long id) throws DoesNotExsistExcpetion;

    public void deleteById(Long id);

    public void deleteAll();

    public PetRequestDto addFriend(long petId, long friendId) throws DoesNotExsistExcpetion;

    public PetRequestDto removeFriend(long petId, long friendId) throws DoesNotExsistExcpetion;

    public PetRequestDto changeOwner(long petId, long oldOwnerId, long newOwnerId) throws DoesNotExsistExcpetion;

    public List<PetRequestDto> getAll();
}
