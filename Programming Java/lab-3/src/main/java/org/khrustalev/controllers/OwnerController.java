package org.khrustalev.controllers;

import org.khrustalev.model.services.OwnerService;
import org.khrustalev.model.services.dto.OwnerDto;
import org.khrustalev.model.services.exceptions.EntityDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/owner")
public class OwnerController {
    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<OwnerDto> createOwner(@RequestBody OwnerDto ownerDto) {
        this.ownerService.save(ownerDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ownerDto);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #ownerDto.id == authentication.principal.owner.id")
    @PutMapping("/update")
    public ResponseEntity<OwnerDto> updateOwner(@RequestBody OwnerDto ownerDto) {
        try {
            this.ownerService.update(ownerDto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ownerDto);
        }
        catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable("id") Long ownerId) {
        try {
            OwnerDto owner = this.ownerService.deleteById(ownerId);

            return ResponseEntity
                    .noContent().build();
        }
        catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #ownerId == authentication.principal.owner.id")
    @PutMapping("/{owner_id}/add-pet/{pet_id}")
    public ResponseEntity<OwnerDto> addPet(@PathVariable("owner_id") Long ownerId,
                                           @PathVariable("pet_id") Long petId) {
        try {
            OwnerDto owner = this.ownerService.addPet(ownerId, petId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(owner);
        }
        catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #ownerId == authentication.principal.owner.id")
    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> getOwner(@PathVariable("id") Long ownerId) {
        try {
            OwnerDto owner = this.ownerService.findById(ownerId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(owner);
        } catch (EntityDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @GetMapping("/all")
    public Page<OwnerDto> getAllOwnersBySortingPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") boolean descending) {
        return this.ownerService.getOwnersBySortingPaging(page, size, sortBy, descending);
    }
}
