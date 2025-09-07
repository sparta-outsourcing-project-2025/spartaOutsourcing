package com.example.spartaoutsourcing.common.aspect;

import com.example.spartaoutsourcing.domain.activity.entity.Activity;
import com.example.spartaoutsourcing.domain.activity.enums.ActivityType;
import com.example.spartaoutsourcing.domain.activity.service.ActivityService;
import com.example.spartaoutsourcing.domain.auth.dto.response.RegisterResponse;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskResponse;
import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;
import com.example.spartaoutsourcing.domain.task.service.TaskService;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.spartaoutsourcing.common.consts.ServiceMethodName.*;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ActivityLoggingAspect {
    private final ActivityService activitiesService;
    private final UserService userService;
    private final TaskService taskService;

    @Pointcut("execution(* com.example.spartaoutsourcing.domain.task.service.TaskService.save(..))")
    private void saveTask() {}//

    @Pointcut("execution(* com.example.spartaoutsourcing.domain.task.service.TaskService.update(..))")
    private void updateTask() {}//

    @Pointcut("execution(* com.example.spartaoutsourcing.domain.task.service.TaskService.delete(..))")
    private void deleteTask(){}

    @Pointcut("execution(* com.example.spartaoutsourcing.domain.task.service.TaskService.updateStatus(..))")
    private void updateTaskStatus() {}

    @Pointcut("execution(* com.example.spartaoutsourcing.domain.comment.service.CommentService.save(..))")
    private void saveComment() {}

    @Pointcut("execution(* com.example.spartaoutsourcing.domain.auth.service.AuthService.login(..))")
    private void login() {}//

    @Pointcut("execution(* com.example.spartaoutsourcing.domain.auth.service.AuthService.register(..))")
    private void register() {}//

    // 작업 로그
    @Around("saveTask() || updateTask() || deleteTask() || updateTaskStatus()")
    public Object logTask(ProceedingJoinPoint pjp) throws Throwable {
        // RequestBody
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
        String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

        // Proceed
        Object result = pjp.proceed();

        // TaskId 매개변수
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = pjp.getArgs();
        Long taskId = 0L;
        if(pjp.getSignature().getName().equals(SAVE_METHOD_NAME)){
            taskId = ((TaskResponse) result).getId();
        }
        else {
            for (int i = 0; i < paramNames.length; i++) {
                if (paramNames[i].equals("taskId")) {
                    taskId = (Long) args[i];
                    break;
                }
            }
        }

        // TaskStatus 일 때 기록
        String beforeTaskStatus = "";
        if(pjp.getSignature().getName().equals("statusUpdate"))
            beforeTaskStatus = taskService.getTaskById(taskId).getTaskStatus().toString();


        // 로그 정보
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.getUserById(userId);
        ActivityType type = null;
        String description = switch (pjp.getSignature().getName()) {
            case SAVE_METHOD_NAME -> {
                TaskResponse taskResponse = (TaskResponse)result;
                type = ActivityType.TASK_CREATED;
                yield type.getDescription()
                        .replace("{1}", taskResponse.getTitle());
            }
            case UPDATE_METHOD_NAME -> {
                type = ActivityType.TASK_UPDATED;
                yield type.getDescription();
            }
            case DELETE_METHOD_NAME -> {
                type = ActivityType.TASK_DELETED;
                yield type.getDescription();
            }
            case  UPDATE_STATUS_METHOD_NAME -> {
                type = ActivityType.TASK_STATUS_CHANGED;
                TaskStatus afterTaskStatus =  ((TaskResponse)result).getStatus();

                yield type.getDescription()
                        .replace("{1}", beforeTaskStatus)
                        .replace("{2}", afterTaskStatus.toString());
            }
            default -> "";
        };

        // 로그 저장
        Activity activity = activitiesService.save(type, user, taskId, description);

        // 로그 출력
        log.info("[{}] UserID:{} | MethodName:{}\nrequestBody : \n{}\nresult : {}",
                activity.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME),
                activity.getUser().getId(),
                pjp.getSignature().getName(),
                requestBody,
                description);

        return result;
    }

    @Around("saveComment()")
    public Object logComment(ProceedingJoinPoint pjp) throws Throwable {
        // TaskId 매개변수
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = pjp.getArgs();
        Long taskId = 0L;
        for(int i=0;i<paramNames.length;i++){
            if(paramNames[i].equals("taskId")){
                taskId = (Long)args[i];
                break;
            }
        }

        // RequestBody
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
        String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

        // Proceed
        Object result = pjp.proceed();

        // 로그 정보
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.getUserById(userId);
        ActivityType type = null;
        String description = switch (pjp.getSignature().getName()) {
            case "saveComment" -> {
                type = ActivityType.COMMENT_CREATED;
                yield type.getDescription();
            }
            default -> "";
        };

        // 로그 저장
        Activity activity = activitiesService.save(type, user, taskId, description);

        // 로그 출력
        log.info("[{}] UserID:{} | MethodName:{}\nrequestBody : \n{}\nresult : {}",
                activity.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME),
                activity.getUser().getId(),
                pjp.getSignature().getName(),
                requestBody,
                description);

        return result;
    }


    // 로그인 로그
    @AfterReturning(pointcut = "login()")
    public void logLogin(JoinPoint jp) throws Throwable {
        // RequestBody
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
        String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

        // GetUser
        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(requestBody);
        String username = json.get("username").toString();
        User user = userService.getUserByUsername(username);

        Long userId = user.getId();

        log.info("[{}] UserID:{} | MethodName:{}\nrequestBody : \n{}\nresult : 로그인 했습니다.",
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                userId,
                jp.getSignature().getName(),
                requestBody);
    }

    // 회원가입 로그
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
    }
}