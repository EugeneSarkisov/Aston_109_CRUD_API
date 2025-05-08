package com.aston.crud_api.repository;

import com.aston.crud_api.model.UserAccount;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    UserAccount findUserAccountByUsername(String username);
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_account SET username = :username, email = :email, age = :age WHERE username = :oldUsername", nativeQuery = true)
    void updateUserAccount(String oldUsername, String username, String email, int age);
}
