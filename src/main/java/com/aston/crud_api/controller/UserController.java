package com.aston.crud_api.controller;

import com.aston.crud_api.dto.request.UserAccountCreationRequest;
import com.aston.crud_api.dto.request.UserAccountUpdateRequest;
import com.aston.crud_api.dto.request.UsernameRequest;
import com.aston.crud_api.dto.response.GettingUserAccountResponse;
import com.aston.crud_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/get")
    public ResponseEntity<GettingUserAccountResponse> getUserInfo(
            @RequestBody UsernameRequest request
            ) {
        return ResponseEntity.ok(service.getUserInfo(request));
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createNewUserAccount(
           @RequestBody UserAccountCreationRequest request
    ){
        return ResponseEntity.ok(service.createUserAccount(request));
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteAccount(
            @RequestBody UsernameRequest request
    ){
        return ResponseEntity.ok(service.deleteUserAccount(request));
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateAccountInfo(
            @RequestBody UserAccountUpdateRequest request
            ){
        return ResponseEntity.ok(service.updateUserAccount(request));
    }
}
