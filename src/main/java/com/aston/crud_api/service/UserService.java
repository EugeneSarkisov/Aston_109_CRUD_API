package com.aston.crud_api.service;

import com.aston.crud_api.dto.request.UserAccountUpdateRequest;
import com.aston.crud_api.dto.request.UsernameRequest;
import com.aston.crud_api.dto.request.UserAccountCreationRequest;
import com.aston.crud_api.dto.response.GettingUserAccountResponse;
import com.aston.crud_api.model.UserAccount;
import com.aston.crud_api.repository.UserAccountRepository;
import com.aston.crud_api.util.kafka.event.UserAccountCreatedEvent;
import com.aston.crud_api.util.kafka.event.UserAccountDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserAccountRepository userAccountRepository;

    @Autowired
    private KafkaTemplate<String, UserAccountCreatedEvent> kafkaCreatedTemplate;

    @Autowired
    private KafkaTemplate<String, UserAccountDeletedEvent> kafkaDeletedTemplate;

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
            UserAccountCreatedEvent accountCreatedEvent = UserAccountCreatedEvent.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .notification(request.getUsername() + " user was successfully created! " + " User email: " + request.getEmail())
                    .build();
            kafkaCreatedTemplate.send("crud-api-events-topic", accountCreatedEvent);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            UserAccountCreatedEvent accountErrorEvent = UserAccountCreatedEvent.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .notification(request.getUsername() + " user wasn't created. Please, try again " + " User email: " + request.getEmail())
                    .build();
            kafkaCreatedTemplate.send("crud-api-error-event-topic", accountErrorEvent);
            return false;
        }
    }

    public boolean deleteUserAccount(UsernameRequest request){
        UserAccount account = userAccountRepository.findUserAccountByUsername(request.getUsername());
        if(account != null){
            userAccountRepository.delete(account);
            UserAccountDeletedEvent accountDeletedEvent = UserAccountDeletedEvent.builder()
                    .username(request.getUsername())
                    .notification(request.getUsername() + " user was successfully deleted!")
                    .build();
            kafkaDeletedTemplate.send("crud-api-events-topic", accountDeletedEvent);
            return true;
        } else {
            UserAccountDeletedEvent accountDeletedEvent = UserAccountDeletedEvent.builder()
                    .username(request.getUsername())
                    .notification(request.getUsername() + " user wasn't deleted. Please, try again.")
                    .build();
            kafkaDeletedTemplate.send("crud-api-events-topic", accountDeletedEvent);
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
