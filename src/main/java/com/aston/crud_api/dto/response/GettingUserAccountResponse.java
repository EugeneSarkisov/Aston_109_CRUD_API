package com.aston.crud_api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GettingUserAccountResponse {
    private long id;
    private String username;
    private String email;
    private int age;
}
