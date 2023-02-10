package com.example.webapp.controllers;

import com.example.webapp.models.Team;
import com.example.webapp.models.User;
import com.example.webapp.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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
    public List<String> list(){
        List<Team> listOfTeam = teamService.listTeams();
        List<String> listOfCodes = new ArrayList<>();
        for (int i = 0; i< listOfTeam.size(); i++){
            listOfCodes.add(listOfTeam.get(i).getCode());
        }
         return listOfCodes;

    }

    @PostMapping("/team/enter")
    public String enterInTeam(@RequestBody Map<String,String> teamId){
        System.out.println(teamId.values().toArray()[0]);
        System.out.println(teamId.values().toArray()[1]);
        User user = new User(teamId.values().toArray()[0].toString(), teamId.values().toArray()[1].toString());
        teamService.enterInTeam(user);
        return teamService.getUsersByCode(user.getTeamId()).toString();
    }

    @GetMapping("/")
    public String teams(@RequestParam(name = "code", required = false) String code, Model model) {
        model.addAttribute("teams", teamService.listTeams());
        return "teams";
    }

    @GetMapping("/team/{code}")
    public String productInfo(@PathVariable String   code, Model model) {
        model.addAttribute("listOfUsers", teamService.getUsersByCode(code));
        return "team-store";
    }
    //
    @PostMapping("/team/create")
    public String createTeam(String username) {
        teamService.saveTeam(username);
        return "redirect:/";
    }
    //

    //
    @PostMapping("/team/delete")
    public String deleteProduct(String code) {
        teamService.deleteTeam(code);
        return "redirect:/";
    }

}
