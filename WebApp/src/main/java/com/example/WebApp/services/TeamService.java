package com.example.webapp.services;

import com.example.webapp.models.Team;
import com.example.webapp.models.User;
import com.example.webapp.repositories.TeamRepository;
import com.example.webapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;


    public List<Team> listTeams() {
        return teamRepository.findAll();
    }

    public List<String> getUsersByCode(String code) {
        List<User> listOfUsers = userRepository.findUsersByTeamId(code);
        List<String> users = new ArrayList<>();

        for (User listOfUser : listOfUsers) {
            users.add(listOfUser.getUsername());
        }
        if (code != null) return users;
        else
            return null;
    }

    public String idGenerator(List<Team> teams) {
        String sAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int firstNumber = (int) Math.round(Math.random() * 9);
        int secondNumber = (int) Math.round(Math.random() * 25);
        int thirdNumber = (int) Math.round(Math.random() * 9);
        int fourthNumber = (int) Math.round(Math.random() * 25);
        String id = Integer.toString(firstNumber) + sAlphabet.charAt(secondNumber)
                + thirdNumber + sAlphabet.charAt(fourthNumber);
        for (int i = 0; i < teams.size(); i++) {
            if (Objects.equals(id, teams.get(i).getCode())) {
                id = idGenerator(teams);
            }
        }
        return id;
    }

    public void saveTeam(String username) {
        log.info("Saving new {}", username);
        String id = idGenerator(teamRepository.findAll());
        teamRepository.save(new Team(id, 1, username));
        userRepository.save(new User(username, id));
    }

    public void deleteTeam(String id) {
        teamRepository.deleteById(id);
    }

    public void enterInTeam(User user) {
        userRepository.save(user);
        System.out.println();

    }

}
