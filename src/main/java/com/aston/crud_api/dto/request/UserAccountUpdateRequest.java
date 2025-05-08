package com.aston.crud_api.dto.request;

import lombok.Data;

@Data
public class UserAccountUpdateRequest {
    private String oldUsername;
    private String username;
    private String email;
    private int age;
}
