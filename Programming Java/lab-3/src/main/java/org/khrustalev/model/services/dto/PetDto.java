package org.khrustalev.model.services.dto;


import org.khrustalev.model.entities.pets.Color;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PetDto {
    private long id;

    private String nickname;
    private LocalDate birthday;
    private String breed;

    private Color color;

    private long ownerId;

    private List<Long> friends = new ArrayList<>();

    public PetDto() {}

    public PetDto(String nickname, LocalDate birthday, String breed, Color color, long ownerId, List<Long> friends) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.breed = breed;
        this.color = color;
        this.ownerId = ownerId;
        this.friends = friends;
    }

    public PetDto(String nickname, LocalDate birthday, String breed, Color color, long ownerId) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.breed = breed;
        this.color = color;
        this.ownerId = ownerId;
    }

    public PetDto(long id, String nickname, LocalDate birthday, String breed, Color color, long ownerId) {
        this.id = id;
        this.nickname = nickname;
        this.birthday = birthday;
        this.breed = breed;
        this.color = color;
        this.ownerId = ownerId;
    }

    public PetDto(long id, String nickname, LocalDate birthday, String breed, Color color, long ownerId, List<Long> friends) {
        this.id = id;
        this.nickname = nickname;
        this.birthday = birthday;
        this.breed = breed;
        this.color = color;
        this.ownerId = ownerId;
        this.friends = friends;
    }

    public long getId() {
        return this.id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
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

    public void addFriend(long id) {
        this.friends.add(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PetDto other = (PetDto) obj;

        return id == other.id;
    }
}
