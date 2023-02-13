package com.example.webapp.controllers;

import com.example.webapp.models.Team;
import com.example.webapp.models.User;
import com.example.webapp.services.TeamService;
import lombok.RequiredArgsConstructor;
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
        for (int i = 0; i < listOfTeam.size(); i++) {
            listOfCodes.add(listOfTeam.get(i).getCode());
        }
        return listOfCodes;

    }

    @PostMapping("/team/enter")
    public String enterInTeam(@RequestBody Map<String, String> teamId) {

        User user = new User(teamId.values().toArray()[0].toString(), teamId.values().toArray()[1].toString());
        if (teamService.userIsUnique(user.getUsername())) {
            System.out.println(user.getUsername());
            teamService.enterInTeam(user);
            return teamService.getUsersByCode(user.getTeamId()).toString();
        }
        else
            return "NO";
    }

    @PostMapping("/team/check")
    public String checkInTeam(@RequestBody Map<String, String> teamId){
        String username = teamId.values().toArray()[0].toString();
        return teamService.getTeamByUsername(username).getTeamId();
    }

    @PostMapping("/team/create")
    public String createTeam(@RequestBody Map<String, String> teamId) {
        String username = teamId.values().toArray()[0].toString();
        if (teamService.adminIsUnique(username)) {
            teamService.saveTeam(username);
            String code = teamService.getTeamByAdminName(username).get(0).getCode();
            return code;
        } else return "NO";
    }

    @PostMapping("/exit")
    public void exitFromTeam(@RequestBody Map<String, String> user) {
        String username = user.values().toArray()[0].toString();
        teamService.exitFromTeam(username);
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
