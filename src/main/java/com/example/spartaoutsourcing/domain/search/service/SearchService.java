package com.example.spartaoutsourcing.domain.search.service;

import com.example.spartaoutsourcing.domain.search.dto.response.*;
import com.example.spartaoutsourcing.domain.task.entity.Task;
import com.example.spartaoutsourcing.domain.task.service.TaskService;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.team.service.TeamService;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final TaskService taskService;
    private final UserService userService;
    private final TeamService teamService;

    /**
     * {@link Task}, {@link User}, {@link Team} 통합 검색
     *
     * @param keyword 조회할 키워드
     * @return 검색 응답 반환
     */
    @Transactional(readOnly = true)
    public IntegratedSearchResponse integratedSearch(String keyword) {
        if(keyword == null || keyword.isEmpty())
        {
            return null;
        }

        List<Task> tasks = taskService.getTasksByKeyword(keyword);
        List<User> users = userService.getUsersByKeyword(keyword);
        List<Team> teams = teamService.getTeamsByKeyword(keyword);

        List<SearchTaskResponse> searchTaskResponses = tasks.stream().map(
                task -> {
                    User taskUser = task.getUser();
                    SearchAssigneeResponse searchAssigneeResponse = SearchAssigneeResponse.of(taskUser.getId(),taskUser.getName());

                    return SearchTaskResponse.of(
                            task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getTaskStatus(),
                            searchAssigneeResponse);
                }
        ).toList();

        List<SearchUserResponse> searchUserResponses = users.stream().map(
                user -> SearchUserResponse.of(
                        user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getEmail()
                )
        ).toList();

        List<SearchTeamResponse> searchTeamResponses = teams.stream().map(
                team -> SearchTeamResponse.of(
                        team.getId(),
                        team.getName(),
                        team.getDescription()
                )
        ).toList();

        return IntegratedSearchResponse.of(searchTaskResponses,searchUserResponses,searchTeamResponses);
    }
}
