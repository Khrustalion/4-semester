package org.khrustalev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDto {
    private long id;

    private String name;
    private LocalDate birthday;

    private List<Long> pets = new ArrayList<>();

    public void addPet(Long petId) {
        pets.add(petId);
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
