package com.example.webapp.repositories;

import com.example.webapp.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, String> {

    List<Team> getTeamByAdminName(String adminName);

    int countByAdminName(String adminName);

}
