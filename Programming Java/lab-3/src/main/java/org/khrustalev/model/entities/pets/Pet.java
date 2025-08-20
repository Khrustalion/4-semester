package org.khrustalev.model.entities.pets;

import jakarta.persistence.*;
import org.khrustalev.model.entities.owners.Owner;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="Pets")
public class Pet {
    @Id
    @GeneratedValue
    private long id;

    private String nickname;
    private LocalDate birthday;
    private String breed;

    @Enumerated(EnumType.STRING)
    private Color color;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToMany
    @JoinTable(
            name = "pet_friends",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Pet> friends;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setFriends(List<Pet> friends) {
        this.friends = friends;
    }

    public List<Pet> getFriends() {
        return this.friends;
    }

    public void addFriend(Pet pet) {
        this.friends.add(pet);
    }

    public void removeFriend(Pet pet) {
        this.friends.remove(pet);
    }
}
