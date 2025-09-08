package com.example.spartaoutsourcing.common.aspect;

import com.example.spartaoutsourcing.domain.activity.entity.Activity;
import com.example.spartaoutsourcing.domain.activity.enums.ActivityType;
import com.example.spartaoutsourcing.domain.activity.service.ActivityService;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskResponse;
import com.example.spartaoutsourcing.domain.task.entity.Task;
import com.example.spartaoutsourcing.domain.task.service.TaskService;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ActivityLoggingAspect {
    private final ActivityService activitiesService;
    private final UserService userService;
    private final TaskService taskService;

    @Pointcut("execution(* com.example.spartaoutsourcing.domain.task.service.TaskService.save(..))")
    private void taskSave() {
    }

    @Pointcut("execution(* com.example.spartaoutsourcing.domain.task.service.TaskService.update(..))")
    private void taskUpdate() {
    }

    //@Pointcut("execution(* com.example.spartaoutsourcing.domain.task.controller.TaskController.save(..))")
    //private void deleteTask(){}

    @Pointcut("execution(* com.example.spartaoutsourcing.domain.task.service.TaskService.statusUpdate(..))")
    private void taskStatusUpdate() {
    }

    @Pointcut("execution(* com.example.spartaoutsourcing.domain.comment.service.CommentService.save(..))")
    private void commentSave() {
    }

    @Pointcut("execution(* com.example.spartaoutsourcing.domain.auth.service.AuthService.login(..))")
    private void login() {
    }

    @Pointcut("execution(* com.example.spartaoutsourcing.domain.auth.service.AuthService.register(..))")
    private void register() {
    }

    @AfterReturning(pointcut = "taskSave() || taskUpdate()", returning = "result")
    public void logTask(JoinPoint jp, Object result) throws Throwable {
        // RequestBody
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
        String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

        // Result
        TaskResponse taskResponse = (TaskResponse) result;
        Long taskId = taskResponse.getId();
        Long userId = taskResponse.getAssigneeId();
        User user = userService.getUserById(userId);
        Task task = taskService.getTesKById(taskId);
        ActivityType type = null;

        String description = switch (jp.getSignature().getName()) {
            case "save" -> {
                type = ActivityType.TASK_CREATED;
                yield type.getDescription()
                        .replace("{1}", taskResponse.getTitle());
            }
            case "update" -> {
                type = ActivityType.TASK_UPDATED;
                yield type.getDescription();
            }
            default -> "";
        };

        // 로그 저장
        Activity activity = activitiesService.save(type, user, task, description);

        log.info("[{}] UserID:{} | MethodName:{}\nrequestBody : \n{}\nresult : {}",
                activity.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME),
                activity.getUser().getId(),
                jp.getSignature().getName(),
                requestBody,
                description);
    }

    /*@Around("taskStatusUpdate()")
    public Object logTaskStatusUpdate(ProceedingJoinPoint pjp) throws Throwable {

        Object result = pjp.proceed();

        /*case "statusUpdate" -> {
            type = ActivityType.TASK_STATUS_CHANGED;
            JSONParser  jsonParser = new JSONParser();
            JSONObject json = (JSONObject) jsonParser.parse(requestBody);
            String afterTaskStatus = json.get("taskStatus").toString();

            yield type.getDescription()
                    .replace("{1}", taskResponse.getStatus().name())
                    .replace("{2}", afterTaskStatus);
        }

        return result;
    }

    @AfterReturning(pointcut = "register()", returning = "result")
    public void logRegister(JoinPoint jp, Object result) throws Throwable {
        // RequestBody
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
        String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

        // Result
        RegisterResponse registerResponse = (RegisterResponse) result;
        Long userId = registerResponse.getId();

        log.info("[{}] UserID:{} | MethodName:{}\nrequestBody : \n{}\nresult : 회원 가입 했습니다.",
                registerResponse.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME),
                userId,
                jp.getSignature().getName(),
                requestBody);
    }*/
}