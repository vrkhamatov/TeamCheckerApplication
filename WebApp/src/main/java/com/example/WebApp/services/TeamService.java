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
import java.lang.Math;
import java.util.UUID;
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

    public double getDistanceBetweenPointsNew(double latitude1, double longitude1, double latitude2, double longitude2) {
        double theta = longitude1 - longitude2;
        double distance = 60 * 1.1515 * (180/Math.PI) * Math.acos(
                Math.sin(latitude1 * (Math.PI/180)) * Math.sin(latitude2 * (Math.PI/180)) +
                        Math.cos(latitude1 * (Math.PI/180)) * Math.cos(latitude2 * (Math.PI/180)) * Math.cos(theta * (Math.PI/180))
        );


        distance = distance * 1.609344*1000;
            return distance;
    }

    public String teamIdGenerator(List<Team> teams) {
        String sAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int firstNumber = (int) Math.round(Math.random() * 9);
        int secondNumber = (int) Math.round(Math.random() * 25);
        int thirdNumber = (int) Math.round(Math.random() * 9);
        int fourthNumber = (int) Math.round(Math.random() * 25);
        String id = Integer.toString(firstNumber) + sAlphabet.charAt(secondNumber)
                + thirdNumber + sAlphabet.charAt(fourthNumber);
        for (int i = 0; i < teams.size(); i++) {
            if (Objects.equals(id, teams.get(i).getCode())) {
                id = teamIdGenerator(teams);
            }
        }
        return id;
    }

    public void saveTeam(String username) {
        log.info("Saving new {}", username);
        String teamId = teamIdGenerator(teamRepository.findAll());
        String userId = java.util.UUID.randomUUID().toString();
        System.out.println(userId);
        teamRepository.save(new Team(teamId,username,userId));
        userRepository.save(new User(userId,username, teamId,200.0,200.0));
    }

    public boolean adminIsUnique(String username) {
        if (teamRepository.countByAdminName(username) == 0) return true;
        else
            return false;
    }

    public boolean userIsUnique(String username) {
        System.out.println(userRepository.countUserByUsername(username));
        if (userRepository.countUserByUsername(username) == 0) return true;
        else
            return false;
    }

    public void deleteTeam(String id) {
        teamRepository.deleteById(id);
    }

    public User getTeamByUsername(String username){
        User user = userRepository.getUserByUsername(username);
        return user;
    }

    public void enterInTeam(User user) {
        if( userRepository.getUserByUsername(user.getUsername())== null)
            userRepository.save(user);
    }

    public List<Team> getTeamByAdminName(String adminName) {
        System.out.println(teamRepository.getTeamByAdminName(adminName));
        return teamRepository.getTeamByAdminName(adminName);
    }

    public void exitFromTeam(String username) {
        String code = teamRepository.getTeamByAdminName(username).get(0).getCode().toString();
        deleteTeam(code);
        userRepository.deleteAllByTeamId(code);
    }


}
