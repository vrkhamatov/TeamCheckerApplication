package com.example.webapp.controllers;

import com.example.webapp.models.Team;
import com.example.webapp.models.User;
import com.example.webapp.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @RequestMapping("teams")
    public List<String> list() {
        List<Team> listOfTeam = teamService.listTeams();
        List<String> listOfCodes = new ArrayList<>();
        for (Team team : listOfTeam) {
            listOfCodes.add(team.getCode());
        }
        return listOfCodes;

    }

    @PostMapping("/team/enter")
    public String enterInTeam(@RequestBody Map<String, String> teamId) {
        String userId = java.util.UUID.randomUUID().toString();
        User user = new User(userId, teamId.values().toArray()[0].toString(), teamId.values().toArray()[1].toString(), 200, 200);
        if (teamService.userIsUnique(user.getUsername())) {
            teamService.enterInTeam(user);
            return teamService.getUsersByCode(user.getTeamId()).toString();
        } else
            return "405";
    }

    @PostMapping("/team/check")
    public String checkInTeam(@RequestBody Map<String, String> teamId) {
        String username = teamId.values().toArray()[0].toString();
        return teamService.getTeamByUsername(username).getTeamId();
    }

    @PostMapping("/team/create")
    public String createTeam(@RequestBody Map<String, String> teamId) {
        String username = teamId.values().toArray()[1].toString();
        String userId = java.util.UUID.randomUUID().toString();
        if (teamService.adminIsUnique(username)) {
            teamService.saveTeam(userId, username);
            return teamService.getTeamByAdminName(username).get(0).getCode();
        } else return "405";
    }

    @PostMapping("/exit")
    public void exitFromTeam(@RequestBody Map<String, String> user) {
        String username = user.values().toArray()[0].toString();
        teamService.exitFromTeam(username);
    }

    @PostMapping("/userId")
    public List<String> getUsersId(@RequestBody Map<String, String> user) {
        String code = teamService.getTeamByUsername(user.values().toArray()[0].toString()).getTeamId();
        return teamService.getUsersByCode(code);
    }


    @GetMapping("/")
    public String teams(@RequestParam(name = "code", required = false) String code, Model model) {
        model.addAttribute("teams", teamService.listTeams());
        return "teams";
    }

    @GetMapping("/team/{code}")
    public String productInfo(@PathVariable String code, Model model) {
        model.addAttribute("listOfUsers", teamService.getUsersByCode(code));
        return "team-store";
    }

    @PostMapping("/team/delete")
    public String deleteProduct(@RequestBody Map<String, String> code) {
        teamService.deleteTeam(code.values().toArray()[0].toString());
        return "redirect:/";
    }

}
