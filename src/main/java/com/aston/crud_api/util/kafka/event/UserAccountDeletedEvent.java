package com.aston.crud_api.util.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountDeletedEvent {
    private String username;
    private String notification;
    private String email;
}
