package com.mnogo.transactions.dto.request;

import com.mnogo.transactions.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {

    @Schema(description = "Имя пользователя", example = "John")
    @Size(max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String username;

    @Schema(description = "Адрес электронной почты", example = "jondoe@gmail.com")
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустым")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(min = 6, max = 255, message = "Длина пароля должна быть от 6 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @Schema(description = "Номер телефона", example = "+79236481418")
    @Size(min = 6, max = 50, message = "Номер телефона должен быть от 6 до 50 символов")
    @NotBlank(message = "Номер телефона не может быть пустым")
    private String phone;

    @Schema(description = "Пол пользователя: M — мужской, F — женский", example = "M", allowableValues = { "M", "F" })
    @NotNull(message = "Пол не может быть пустым")
    private Gender gender;

    @Schema(description = "Путь к фотографии пользователя (опционально)", example = "avatars/1.png")
    private String avatarPath;
}
