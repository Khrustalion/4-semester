package org.khrustalev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AddFriendDto {
    private PetDto pet;
    private PetDto friend;

    public AddFriendDto() {}

    public AddFriendDto(PetDto pet, PetDto friend) {
        this.pet = pet;
        this.friend = friend;
    }
}
