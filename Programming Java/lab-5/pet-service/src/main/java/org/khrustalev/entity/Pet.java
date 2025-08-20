package org.khrustalev.entity;

import lombok.Getter;
import org.khrustalev.dto.Color;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pets")
@Data
@NoArgsConstructor
public class Pet {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    @Getter
    @Column(nullable = false)
    private String nickname;
    @Getter
    private LocalDate birthday;
    @Getter
    private String breed;

    @Getter
    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(name = "owner_id", nullable = false)
    @Getter
    private Long ownerId;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "pet_friends",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @Getter
    private List<Pet> friends = new ArrayList<>();

    public void addFriend(Pet pet) {
        this.friends.add(pet);
    }

    public void removeFriend(Pet pet) {
        this.friends.remove(pet);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Pet> getFriends() {
        return friends;
    }

    public void setFriends(List<Pet> friends) {
        this.friends = friends;
    }
}
