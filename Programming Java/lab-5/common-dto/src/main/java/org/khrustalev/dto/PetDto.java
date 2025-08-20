package org.khrustalev.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDto {
    @Getter
    private long id;
    @Getter
    private String nickname;
    @Getter
    private LocalDate birthday;
    @Getter
    private String breed;
    @Getter
    private Color color;
    @Getter
    private long ownerId;
    @Getter
    private List<Long> friends = new ArrayList<>();

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
