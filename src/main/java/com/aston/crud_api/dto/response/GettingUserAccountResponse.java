package com.aston.crud_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Schema(description = "Response if user account are existing")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GettingUserAccountResponse extends RepresentationModel<GettingUserAccountResponse> {
    @Schema(description = "Existent user id", example = "1")
    private long id;
    @Schema(description = "Existent username", example = "John")
    private String username;
    @Schema(description = "User email", example = "111@gmail.test")
    private String email;
    @Schema(description = "User age", example = "22")
    private int age;
}
