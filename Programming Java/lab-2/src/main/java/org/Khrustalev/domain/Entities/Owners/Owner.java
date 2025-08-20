package org.Khrustalev.domain.Entities.Owners;

import jakarta.persistence.*;
import org.Khrustalev.domain.Entities.Pets.Pet;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="Owners")
public class Owner {
    @Id
    @GeneratedValue
    private long id;

    private String name;
    private LocalDate birthday;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Pet> pets;

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public String getName() {
        return this.name;
    }

    public void setBirthday(LocalDate Birthday) {
        this.birthday = Birthday;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public void setPets(List<Pet> Pets) {
        this.pets = Pets;
    }

    public List<Pet> getPets() {
        return this.pets;
    }

    public void addPet(Pet pet) {
        this.pets.add(pet);
    }

    public void removePet(Pet pet) {
        this.pets.remove(pet);
    }
}
