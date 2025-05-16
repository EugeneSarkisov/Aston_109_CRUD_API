package com.aston.crud_api.controller;

import com.aston.crud_api.dto.request.UserAccountCreationRequest;
import com.aston.crud_api.dto.request.UserAccountUpdateRequest;
import com.aston.crud_api.dto.request.UsernameRequest;
import com.aston.crud_api.dto.response.GettingUserAccountResponse;
import com.aston.crud_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "User operations", description = "User CRUD controller")
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @Operation(summary = "Get user account info")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found existing user account",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = GettingUserAccountResponse.class))
                            )
                    })
    })
    @GetMapping("/get")
    public ResponseEntity<GettingUserAccountResponse> getUserInfo(
            @RequestBody @Parameter(name = "UsernameRequest", description = "Existing username DTO")
            UsernameRequest request
    ) {
        GettingUserAccountResponse response = service.getUserInfo(request);
        Link deleteLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deleteAccount(request))
                .withRel("/delete");
        response.add(deleteLink);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create new user account")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully created new user account",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "boolean")
                            )
                    })
    })
    @PutMapping("/create")
    public ResponseEntity<GettingUserAccountResponse> createNewUserAccount(
            @RequestBody @Parameter(name = "UserAccountCreationRequest", description = "New User Account DTO")
            UserAccountCreationRequest request
    ) {
        GettingUserAccountResponse response = service.createUserAccount(request);
        Link getInfoLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserInfo(new UsernameRequest(request.getUsername())))
                .withRel("/get");
        response.add(getInfoLink);
        Link deleteLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deleteAccount(new UsernameRequest(request.getUsername())))
                .withRel("/delete");
        response.add(deleteLink);
        return ResponseEntity.ok(service.createUserAccount(request));
    }

    @Operation(summary = "Delete existing user account")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted user account",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "boolean")
                            )
                    })
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteAccount(
            @RequestBody @Parameter(name = "UsernameRequest", description = "Existing username DTO")
            UsernameRequest request
    ) {
        return ResponseEntity.ok(service.deleteUserAccount(request));
    }

    @Operation(summary = "Update existing user account info")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully update user account info",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "boolean")
                            )
                    })
    })
    @PostMapping("/update")
    public ResponseEntity<Boolean> updateAccountInfo(
            @RequestBody @Parameter(name = "UserAccountUpdateRequest", description = "Existing user account with new info DTO")
            UserAccountUpdateRequest request
    ) {
        return ResponseEntity.ok(service.updateUserAccount(request));
    }
}
