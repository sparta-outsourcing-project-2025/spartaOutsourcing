package com.example.spartaoutsourcing.domain.search.service;

import com.example.spartaoutsourcing.domain.search.dto.response.*;
import com.example.spartaoutsourcing.domain.task.entity.Task;
import com.example.spartaoutsourcing.domain.task.service.TaskService;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.team.service.TeamService;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public IntegratedSearchResponse getIntegratedSearchResponseByKeyword(String keyword) {
        if(keyword == null || keyword.isEmpty())
        {
            return null;
        }

        List<Task> tasks = taskService.getTasksByKeyword(keyword);
        List<User> users = userService.getUsersByKeyword(keyword);
        List<Team> teams = teamService.getTeamsByKeyword(keyword);

        List<TaskSearchResponse> searchTaskResponses = tasks.stream().map(
                task -> {
                    User taskUser = task.getUser();
                    AssigneeSearchResponse searchAssigneeResponse = AssigneeSearchResponse.of(taskUser.getId(),taskUser.getName());

                    return TaskSearchResponse.of(
                            task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getTaskStatus(),
                            searchAssigneeResponse
                    );
                }
        ).toList();

        List<UserSearchResponse> searchUserResponses = users.stream().map(
                user -> UserSearchResponse.of(
                        user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getEmail()
                )
        ).toList();

        List<TeamSearchResponse> searchTeamResponses = teams.stream().map(
                team -> TeamSearchResponse.of(
                        team.getId(),
                        team.getName(),
                        team.getDescription()
                )
        ).toList();

        return IntegratedSearchResponse.of(searchTaskResponses,searchUserResponses,searchTeamResponses);
    }

    /**
     * {@link Task} 페이지 검색
     *
     * @param keyword 조회할 키워드
     * @param pageable 페이지 옵션
     * @return {@link Task} 검색 응답 반환
     */
    public TaskPageSearchResponse getTaskPageSearchResponse(String keyword, Pageable pageable) {
        Page<Task> taskPage = taskService.getTaskPageByKeyword(keyword, pageable);
        List<Task> tasks =  taskPage.getContent();

        List<TaskContentSearchResponse> taskContentSearchResponses = tasks.stream().map(
                task -> {
                    AssigneeSearchResponse assignee = AssigneeSearchResponse.of(
                            task.getUser().getId(),
                            task.getUser().getUsername());

                    return TaskContentSearchResponse.of(
                            task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getTaskStatus(),
                            task.getTaskPriority(),
                            task.getUser().getId(),
                            assignee,
                            task.getCreatedAt(),
                            task.getModifiedAt(),
                            task.getDueDate()
                    );
                }
        ).toList();

        return TaskPageSearchResponse.of(
                taskContentSearchResponses,
                taskPage.getTotalElements(),
                taskPage.getTotalPages(),
                pageable.getPageSize(),
                pageable.getPageNumber()
        );
    }
}
