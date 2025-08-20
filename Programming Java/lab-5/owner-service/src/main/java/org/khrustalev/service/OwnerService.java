package org.khrustalev.service;

import org.khrustalev.dto.OwnerDto;
import org.khrustalev.dto.Page.PageResponseDto;
import org.khrustalev.dto.Page.PageResponseMapper;
import org.khrustalev.entity.Owner;
import org.khrustalev.exception.OwnerDoesNotExistException;
import org.khrustalev.mapper.OwnerMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.khrustalev.repository.OwnerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public OwnerService(OwnerRepository ownerRepository,
                        OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
    }

    public OwnerDto save(OwnerDto dto) {
        Owner owner = ownerMapper.toEntity(dto);
        owner.setId(null);

        Owner saved = ownerRepository.save(owner);

        return ownerMapper.toDto(saved);
    }

    public OwnerDto update(OwnerDto dto) {
        if (!ownerRepository.existsById(dto.getId())) {
            throw new OwnerDoesNotExistException("Owner with id " + dto.getId() + " does not exist");
        }
        Owner owner = ownerMapper.toEntity(dto);
        Owner updated = ownerRepository.save(owner);
        return ownerMapper.toDto(updated);
    }

    public OwnerDto findById(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerDoesNotExistException("Owner with id " + id + " not found"));
        return ownerMapper.toDto(owner);
    }

    public List<OwnerDto> findAll() {
        return ownerRepository.findAll()
                .stream()
                .map(ownerMapper::toDto)
                .collect(Collectors.toList());
    }

    public PageResponseDto<OwnerDto> findAll(Pageable pageable) {
        Page<OwnerDto> page = ownerRepository.findAll(pageable)
                .map(ownerMapper::toDto);

        return PageResponseMapper.map(page);
    }

    public OwnerDto deleteById(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerDoesNotExistException("Owner with id " + id + " not found"));
        OwnerDto dto = ownerMapper.toDto(owner);
        ownerRepository.delete(owner);
        return dto;
    }
}
