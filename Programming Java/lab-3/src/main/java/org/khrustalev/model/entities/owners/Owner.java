package org.khrustalev.model.entities.owners;

import jakarta.persistence.*;
import lombok.Data;
import org.khrustalev.model.entities.pets.Pet;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name="Owners")
public class Owner {
    @Id
    @GeneratedValue
    @Column(name="owner_id")
    private long id;

    private String name;
    private LocalDate birthday;

    @OneToMany(mappedBy = "owner")
    private List<Pet> pets;

    public void addPet(Pet pet) {
        this.pets.add(pet);
    }

    public void removePet(Pet pet) {
        this.pets.remove(pet);
    }
}
