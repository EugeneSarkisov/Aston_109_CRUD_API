package com.aston.crud_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Request to update existent user account")
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountUpdateRequest {
    @Schema(description = "Existent username", example = "John")
    private String oldUsername;
    @Schema(description = "New username", example = "Rut")
    private String username;
    @Schema(description = "New user email", example = "111@gmail.test")
    private String email;
    @Schema(description = "New user age", example = "23")
    private int age;
}
