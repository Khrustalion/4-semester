package org.khrustalev.mapper;

import org.khrustalev.dto.OwnerDto;
import org.khrustalev.entity.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {

    public OwnerDto toDto(Owner entity) {
        OwnerDto dto = new OwnerDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBirthday(entity.getBirthday());
        return dto;
    }

    public Owner toEntity(OwnerDto dto) {
        Owner entity = new Owner();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setBirthday(dto.getBirthday());
        return entity;
    }
}
