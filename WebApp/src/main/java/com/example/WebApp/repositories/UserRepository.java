package com.example.webapp.repositories;

import com.example.webapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findUsersByTeamId(String code);

    void deleteAllByTeamId(String code);

    int countUserByUsername(String username);

    User getUserByUsername(String username);


}
