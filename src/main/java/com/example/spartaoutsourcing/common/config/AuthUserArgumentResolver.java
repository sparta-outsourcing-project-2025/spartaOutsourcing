package com.example.spartaoutsourcing.common.config;

import com.example.spartaoutsourcing.common.annotation.Auth;
import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthAnnotation=parameter.getParameterAnnotation(Auth.class) != null;
        boolean isAuthUserType=parameter.getParameterType().equals(AuthUserRequest.class);

        if(hasAuthAnnotation != isAuthUserType) {
            throw new GlobalException(ErrorCode.AUTH_ANNOTATION_MISMATCH_TYPE);
        }

        return hasAuthAnnotation;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Long id = (Long) request.getAttribute("userId");
        String email = (String) request.getAttribute("email");
        UserRole role= (UserRole) request.getAttribute("userRole");

        return AuthUserRequest.of(id,email,role);
    }
}