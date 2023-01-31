package com.example.webapp.controllers;

import com.example.webapp.models.User;
import com.example.webapp.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

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
    @PostMapping("/team/enter")
    public String enterInTeam(User user){
        teamService.enterInTeam(user);
        return "redirect:/";
    }
    //
    @PostMapping("/team/delete")
    public String deleteProduct(String code) {
        teamService.deleteTeam(code);
        return "redirect:/";
    }

}
