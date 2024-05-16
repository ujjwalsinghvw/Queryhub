package com.vwits.qaplatform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDto {
    @NotBlank(message = "name can't be null")
    @Pattern(regexp = "\\D+", message = "name must be a string")
    private String name;

    @NotBlank(message = "email can't be null")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "password can't be null")
    @Size(min = 6, message = "password should be minimum 6 characters long")
    private String password;
}
