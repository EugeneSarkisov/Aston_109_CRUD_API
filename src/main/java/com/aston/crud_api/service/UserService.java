package com.aston.crud_api.service;

import com.aston.crud_api.dto.request.UserAccountUpdateRequest;
import com.aston.crud_api.dto.request.UsernameRequest;
import com.aston.crud_api.dto.request.UserAccountCreationRequest;
import com.aston.crud_api.dto.response.GettingUserAccountResponse;
import com.aston.crud_api.model.UserAccount;
import com.aston.crud_api.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAccountRepository userAccountRepository;

    public GettingUserAccountResponse getUserInfo(UsernameRequest request){
        UserAccount account = userAccountRepository.findUserAccountByUsername(request.getUsername());
        return GettingUserAccountResponse.builder()
                .username(account.getUsername())
                .email(account.getEmail())
                .age(account.getAge())
                .build();
    }

    public boolean createUserAccount(UserAccountCreationRequest request){
        try {
            UserAccount account = UserAccount.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .age(request.getAge())
                    .createdAt(new Date())
                    .build();
            userAccountRepository.save(account);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUserAccount(UsernameRequest request){
        UserAccount account = userAccountRepository.findUserAccountByUsername(request.getUsername());
        if(account != null){
            userAccountRepository.delete(account);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateUserAccount(UserAccountUpdateRequest request){
        UserAccount account = userAccountRepository.findUserAccountByUsername(request.getOldUsername());
        if(account != null){
            account = UserAccount.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .age(request.getAge())
                    .build();
            userAccountRepository.updateUserAccount(request.getOldUsername(), account.getUsername(), account.getEmail(), account.getAge());
            return true;
        } else {
            return false;
        }
    }
}
