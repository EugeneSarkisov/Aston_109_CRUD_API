package com.aston.crud_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_account")
@Schema(description = "UserAccount Data Model")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "username")
    @Schema(description = "Username", example = "John")
    private String username;
    @Column(name = "email")
    @Schema(description = "User email", example = "111@gmail.test")
    private String email;
    @Column(name = "age")
    @Schema(description = "User age", example = "22")
    private int age;
    @Column(name = "created_at")
    @Schema(description = "Timestamp which save user creation time in DB", example = "2025-05-15 01:13:14.566")
    private Date createdAt;
}
