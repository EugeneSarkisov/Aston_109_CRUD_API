package com.aston.crud_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Username request to get or delete account")
@AllArgsConstructor
@NoArgsConstructor
public class UsernameRequest {
    @Schema(description = "Existent username", example = "John")
    String username;
}
