package org.Khrustalev.Presentation.dto;

import java.time.LocalDate;
import java.util.List;

public class OwnerRequestDto {
    private long id;

    private String name;
    private LocalDate birthday;

    private List<Long> pets;

    public OwnerRequestDto(String name, LocalDate birthday, List<Long> pets) {
        this.name = name;
        this.birthday = birthday;
        this.pets = pets;
    }

    public OwnerRequestDto(long id, String name, LocalDate birthday, List<Long> pets) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.pets = pets;
    }

    public long getId(){
        return this.id;
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

    public void setPets(List<Long> Pets) {
        this.pets = Pets;
    }

    public List<Long> getPets() {
        return this.pets;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OwnerRequestDto other = (OwnerRequestDto) obj;

        return id == other.id;
    }
}
