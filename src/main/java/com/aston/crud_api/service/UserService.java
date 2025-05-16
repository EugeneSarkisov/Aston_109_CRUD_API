package com.aston.crud_api.service;

import com.aston.crud_api.dto.request.UserAccountUpdateRequest;
import com.aston.crud_api.dto.request.UsernameRequest;
import com.aston.crud_api.dto.request.UserAccountCreationRequest;
import com.aston.crud_api.dto.response.GettingUserAccountResponse;
import com.aston.crud_api.model.UserAccount;
import com.aston.crud_api.repository.UserAccountRepository;
import com.aston.crud_api.util.kafka.event.UserAccountCreatedEvent;
import com.aston.crud_api.util.kafka.event.UserAccountDeletedEvent;
import com.aston.crud_api.util.user_exceptions.IncorrectInputException;
import com.aston.crud_api.util.user_exceptions.UsernameAlreadyExistException;
import com.aston.crud_api.util.validation.Validator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    public static final Logger LOGGER = LogManager.getLogger();
    public static final Logger LOGGER_DEBUG = LogManager.getLogger("org.example");

    public GettingUserAccountResponse getUserInfo(UsernameRequest request) {
        try {
            LOGGER.info("starting getting user info process in table '{}'", "user");
            if (request == null) {
                throw new EntityNotFoundException("User isn't exist! Please, try again.");
            }
            UserAccount account = userAccountRepository.findUserAccountByUsername(request.getUsername());
            LOGGER.info("successfully got info about user '{}'", request.getUsername());
            return GettingUserAccountResponse.builder()
                    .username(account.getUsername())
                    .email(account.getEmail())
                    .age(account.getAge())
                    .build();
        } catch (EntityNotFoundException e) {
            LOGGER_DEBUG.error("failed to create new user. please, check logs");
            LOGGER_DEBUG.error(e.getMessage());
            e.printStackTrace();
            LOGGER_DEBUG.debug(e.getMessage());
            LOGGER_DEBUG.debug(e.getStackTrace());
            return null;
        } catch (Exception e) {
            LOGGER_DEBUG.error("failed to create new user. please, check logs");
            LOGGER_DEBUG.error("Something went wrong... Please, contact with the administrator");
            e.printStackTrace();
            LOGGER_DEBUG.debug(e.getMessage());
            LOGGER_DEBUG.debug(e.getStackTrace());
            return null;
        }
    }

    public GettingUserAccountResponse createUserAccount(UserAccountCreationRequest request) {
        try {
            LOGGER.info("starting creating new user process in table '{}'", "user");
            LOGGER.info("starting validation process");
            Validator.isUsernameValid(request.getUsername());
            Validator.isAgeValid(request.getAge());
            LOGGER.info("checking username accessibility");
            if (userAccountRepository.findUserAccountByUsername(request.getUsername()) != null) {
                throw new UsernameAlreadyExistException("Username " + request.getUsername() + " is already exist");
            }
            UserAccount account = UserAccount.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .age(request.getAge())
                    .createdAt(new Date())
                    .build();
            userAccountRepository.save(account);
            LOGGER.info("sending notification on user email: " + request.getEmail());
            UserAccountCreatedEvent accountCreatedEvent = UserAccountCreatedEvent.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .notification(request.getUsername() + " user was successfully created! " + " User email: " + request.getEmail())
                    .build();
            kafkaCreatedTemplate.send("crud-api-events-topic", accountCreatedEvent);
            LOGGER.info("successfully created new user");
            return GettingUserAccountResponse.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .age(request.getAge())
                    .build();
        } catch (Exception e) {
            LOGGER_DEBUG.error("failed to create new user. please, check logs");
            System.out.println("Please, check inputs and try again.");
            LOGGER_DEBUG.debug(e.getMessage());
            LOGGER_DEBUG.debug(e.getStackTrace());
            e.printStackTrace();
            LOGGER_DEBUG.info("sending notification on user email");
            UserAccountCreatedEvent accountErrorEvent = UserAccountCreatedEvent.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .notification(request.getUsername() + " user wasn't created. Please, try again " + " User email: " + request.getEmail())
                    .build();
            kafkaCreatedTemplate.send("crud-api-error-event-topic", accountErrorEvent);
            return null;
        }
    }

    public boolean deleteUserAccount(UsernameRequest request) {
        try {
            LOGGER.info("starting deleting user process in table '{}'", "user");
            LOGGER.info("checking user existence");
            UserAccount account = userAccountRepository.findUserAccountByUsername(request.getUsername());
            if (account != null) {
                userAccountRepository.delete(account);
                UserAccountDeletedEvent accountDeletedEvent = UserAccountDeletedEvent.builder()
                        .username(request.getUsername())
                        .notification(request.getUsername() + " user was successfully deleted!")
                        .build();
                kafkaDeletedTemplate.send("crud-api-events-topic", accountDeletedEvent);
                return true;
            } else {
                throw new EntityNotFoundException("User isn't exist! Please, try again.");
            }
        } catch (Exception e) {
            LOGGER_DEBUG.error("failed to create new user. please, check logs");
            LOGGER_DEBUG.error(e.getMessage());
            e.printStackTrace();
            LOGGER_DEBUG.debug(e.getMessage());
            LOGGER_DEBUG.debug(e.getStackTrace());
            LOGGER_DEBUG.info("sending notification on user email");
            UserAccountDeletedEvent accountDeletedEvent = UserAccountDeletedEvent.builder()
                    .username(request.getUsername())
                    .notification(request.getUsername() + " user wasn't deleted. Please, try again.")
                    .build();
            kafkaDeletedTemplate.send("crud-api-events-topic", accountDeletedEvent);
            return false;
        }
    }

    public boolean updateUserAccount(UserAccountUpdateRequest request) {
        try {
            LOGGER.info("starting updating user info process in table '{}'", "user");
            LOGGER.info("starting validation process");
            Validator.isUsernameValid(request.getUsername());
            Validator.isAgeValid(request.getAge());
            LOGGER.info("checking new username accessibility");
            if (userAccountRepository.findUserAccountByUsername(request.getUsername()) != null) {
                throw new UsernameAlreadyExistException("Username " + request.getUsername() + " is already exist");
            }
            UserAccount account = userAccountRepository.findUserAccountByUsername(request.getOldUsername());
            if (account != null) {
                account = UserAccount.builder()
                        .username(request.getUsername())
                        .email(request.getEmail())
                        .age(request.getAge())
                        .build();
                userAccountRepository.updateUserAccount(request.getOldUsername(), account.getUsername(), account.getEmail(), account.getAge());
                return true;
            } else {
                throw new EntityNotFoundException("User isn't exist! Please, try again.");
            }
        } catch (EntityNotFoundException | IncorrectInputException e) {
            LOGGER_DEBUG.error("failed to create new user. please, check logs");
            LOGGER_DEBUG.error(e.getMessage());
            e.printStackTrace();
            LOGGER_DEBUG.debug(e.getMessage());
            LOGGER_DEBUG.debug(e.getStackTrace());
            return false;
        } catch (Exception e){
            LOGGER_DEBUG.error("failed to create new user. please, check logs");
            LOGGER_DEBUG.error("Something went wrong... Please, contact with the administrator");
            e.printStackTrace();
            LOGGER_DEBUG.debug(e.getMessage());
            LOGGER_DEBUG.debug(e.getStackTrace());
            return false;
        }
    }
}

