package org.khrustalev.model.services.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class OwnerDto {
    private long id;

    private String name;
    private LocalDate birthday;

    private List<Long> pets = new ArrayList<>();

    public OwnerDto() {}

    public OwnerDto(String name, LocalDate birthday, List<Long> pets) {
        this.name = name;
        this.birthday = birthday;
        this.pets = pets;
    }

    public OwnerDto(String name, LocalDate birthday) {
        this.name = name;
        this.birthday = birthday;
    }
    public OwnerDto(long id, String name, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
    }

    public OwnerDto(long id, String name, LocalDate birthday, List<Long> pets) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.pets = pets;
    }

    public void addPet(long PetId) {
        this.pets.add(PetId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OwnerDto other = (OwnerDto) obj;

        return id == other.id;
    }
}
