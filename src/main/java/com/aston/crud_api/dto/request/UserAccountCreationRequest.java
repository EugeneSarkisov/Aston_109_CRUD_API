package com.aston.crud_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountCreationRequest {
    private String username;
    private String email;
    private int age;
}
