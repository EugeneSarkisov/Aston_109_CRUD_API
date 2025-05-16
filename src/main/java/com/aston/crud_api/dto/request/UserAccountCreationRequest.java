package com.aston.crud_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Request to create new user account")
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountCreationRequest {
    @Schema(description = "New Username", example = "John")
    private String username;
    @Schema(description = "User email", example = "111@gmail.test")
    private String email;
    @Schema(description = "User age", example = "22")
    private int age;
}
