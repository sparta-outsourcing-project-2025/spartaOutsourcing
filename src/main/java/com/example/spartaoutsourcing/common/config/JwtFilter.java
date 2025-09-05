package com.example.spartaoutsourcing.common.config;

import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();

        // 회원가입/로그인 예외
        if (url.equals("/api/auth/register") || url.equals("/api/auth/login")) {
            chain.doFilter(request, response);
            return;
        }

        String bearerJwt = httpRequest.getHeader("Authorization");
        if (bearerJwt == null || bearerJwt.isEmpty()) {
            sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED, "인증 헤더 누락");
            return;
        }

        try {
            String jwt = jwtUtil.substringToken(bearerJwt);
            Claims claims = jwtUtil.extractClaims(jwt);

            if (claims == null) {
                sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED, "Claims 추출 실패");
                return;
            }

            Long userId = Long.parseLong(claims.getSubject());
            String email = claims.get("email", String.class);
            UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

            // SecurityContext 저장
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            email,
                            List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()))
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // AuthUserArgumentResolver용 request attribute
            httpRequest.setAttribute("userId", userId);
            httpRequest.setAttribute("email", email);
            httpRequest.setAttribute("userRole", userRole);

            // 관리자 권한 체크
            if (url.startsWith("/admin") && !UserRole.ADMIN.equals(userRole)) {
                sendErrorResponse(httpResponse, HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
                return;
            }

            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED, "토큰 만료");
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException e) {
            sendErrorResponse(httpResponse, HttpStatus.BAD_REQUEST, "유효하지 않은 토큰");
        } catch (Exception e) {
            log.error("예상치 못한 오류: URI={}", url, e);
            sendErrorResponse(httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, "서버 처리 중 오류 발생");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.name());
        errorResponse.put("code", status.value());
        errorResponse.put("message", message);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
