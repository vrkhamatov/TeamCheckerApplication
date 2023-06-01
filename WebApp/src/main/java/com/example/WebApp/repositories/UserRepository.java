package com.example.webapp.repositories;

import com.example.webapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findUsersByTeamId(String code);

    void deleteAllByTeamId(String code);

    int countUserByUsername(String username);

    User getUserByUsername(String username);

    @Query("SELECT id FROM User WHERE teamId = ?1")
    List<String> getIdsByTeamCode(String teamId);

}
