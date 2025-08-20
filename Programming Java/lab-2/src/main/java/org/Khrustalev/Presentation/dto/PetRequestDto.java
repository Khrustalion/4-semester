package org.Khrustalev.Presentation.dto;

import org.Khrustalev.domain.Entities.Pets.Color;

import java.time.LocalDate;
import java.util.List;

public class PetRequestDto {
    private long id;

    private String nickname;
    private LocalDate birthday;
    private String breed;

    private Color color;

    private long ownerId;

    private List<Long> friends;

    public PetRequestDto(String nickname, LocalDate birthday, String breed, long ownerId, List<Long> friends) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.breed = breed;
        this.ownerId = ownerId;
        this.friends = friends;
    }

    public PetRequestDto(long id, String nickname, LocalDate birthday, String breed, long ownerId, List<Long> friends) {
        this.id = id;
        this.nickname = nickname;
        this.birthday = birthday;
        this.breed = breed;
        this.ownerId = ownerId;
        this.friends = friends;
    }

    public long getId() {
        return this.id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getnickname() {
        return this.nickname;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getBreed() {
        return this.breed;
    }

    public void setOwner(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getOwnerId() {
        return this.ownerId;
    }

    public void setFriends(List<Long> friends) {
        this.friends = friends;
    }

    public List<Long> getFriends() {
        return this.friends;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PetRequestDto other = (PetRequestDto) obj;

        return id == other.id;
    }
}
