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
        String userId = java.util.UUID.randomUUID().toString();
        System.out.println(userId);
        User user = new User(userId, teamId.values().toArray()[0].toString(), teamId.values().toArray()[1].toString(), 200, 200);
        if (teamService.userIsUnique(user.getUsername())) {
            System.out.println(user.getUsername());
            teamService.enterInTeam(user);
            return teamService.getUsersByCode(user.getTeamId()).toString();
        } else
            return "NO";
    }

    @PostMapping("/team/location")
    public String verificationByLocation(@RequestBody Map<String, String> location) {
        String latitudeStr = (String) location.values().toArray()[0];
        String longitudeStr = (String) location.values().toArray()[1];
        double longitude = Double.parseDouble(longitudeStr);
        double latitude = Double.parseDouble(latitudeStr);
        System.out.println(teamService.getDistanceBetweenPointsNew(latitude, longitude, 59.869907, 30.306994));
        if (teamService.getDistanceBetweenPointsNew(latitude, longitude, 59.869907, 30.306994) < 150.0)
            return "Верификация прошла успешно";
        else
            return "Вы находитесь слишком далеко от места регистрации комнаты\n Верификация отклонена";
    }

    @PostMapping("/team/check")
    public String checkInTeam(@RequestBody Map<String, String> teamId) {
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

//    @PostMapping("/adminLocation")
//    public String sendAdminLocation(@RequestBody Map<String, String> username) {
//        if (teamService.getTeamByAdminName(username.values().toArray()[0].toString()).size() == 0)
//            return "NO";
//        else {
//            double latitude = Double.parseDouble(username.values().toArray()[1].toString());
//            double longitude = Double.parseDouble(username.values().toArray()[2].toString());
//
//        }
//
//    }

}
