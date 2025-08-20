package org.khrustalev.controllers;

import org.khrustalev.model.entities.users.User;
import org.khrustalev.model.services.OwnerService;
import org.khrustalev.model.services.PetService;
import org.khrustalev.model.services.dto.PetDto;
import org.khrustalev.model.services.exceptions.EntityDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #petDto.ownerId == authentication.principal.owner.id")
    @PostMapping("/new")
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto petDto) {
        this.petService.save(petDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(petDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #petDto.ownerId == authentication.principal.owner.id")
    @PutMapping("/update")
    public ResponseEntity<PetDto> updatePet(@RequestBody PetDto petDto) {
        try {
            this.petService.update(petDto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(petDto);
        }
        catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable("id") Long id) {
        SecurityAccess(id);

        try {
            this.petService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @PutMapping("/{pet_id}/add-friend/{friend_id}")
    public ResponseEntity<PetDto> addFriend(@PathVariable long pet_id, @PathVariable long friend_id) {
        SecurityAccess(pet_id);

        return ResponseEntity.ok(petService.addFriend(pet_id, friend_id));
    }

    private void SecurityAccess(@PathVariable long pet_id) {
        PetDto pet = petService.findById(pet_id);
        Long ownerId = pet.getOwnerId();

        User userDetails = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (!isAdmin && !ownerId.equals(userDetails.getOwner().getId())) {
            throw new AccessDeniedException("You don't have permission to delete this pet");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPet(@PathVariable("id") int petId) {
        try {
            PetDto pet =  this.petService.findById(petId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(pet);
        }
        catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    @GetMapping("/all")
    public Page<PetDto> getAllPetsBySortingPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") boolean descending) {
        return this.petService.getPetsBySortingPaging(page, size, sortBy, descending);
    }
}
