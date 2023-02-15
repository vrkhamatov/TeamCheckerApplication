package com.example.webapp.repositories;

import com.example.webapp.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, String> {

    public List<Team> getTeamByAdminName(String adminName);

    public void deleteTeamByAdminName(String adminName);

    public void deleteAllByCode(String code);

    public int countByAdminName(String adminName);


}
