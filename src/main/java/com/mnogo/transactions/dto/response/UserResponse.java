package com.mnogo.transactions.dto.response;

import com.mnogo.transactions.entity.User;
import com.mnogo.transactions.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о пользователе за исключением id и password")
public class UserResponse {

    private String username;

    private String email;

    private String phone;

    private Gender gender;

    @Schema(example = "avatars/1.png")
    private String avatarPath;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.gender = user.getGender();
        this.avatarPath = user.getAvatarPath();
    }
}
