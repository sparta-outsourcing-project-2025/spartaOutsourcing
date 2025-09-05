package com.example.spartaoutsourcing.common.config;

import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String url = request.getRequestURI();

        // /api/auth/register, /api/auth/login 은 JWT 검증 제외
        if (url.equals("/api/auth/register") || url.equals("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (url.equals("/api")) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerJwt = request.getHeader("Authorization");

        // 운영 환경 기준: JWT 없으면 401 Unauthorized
        if (bearerJwt == null || bearerJwt.isEmpty()) {
            log.warn("인증 헤더 누락: URI={}", url);
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
            return;
        }

        String jwt = jwtUtil.substringToken(bearerJwt);

        try {
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                log.warn("Claims 추출 실패: URI={}", url);
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
                return;
            }

            UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

            // JWT에서 추출한 정보 request에 주입
            request.setAttribute("userId", Long.parseLong(claims.getSubject()));
            request.setAttribute("email", claims.get("email"));
            request.setAttribute("userRole", userRole);

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.info("JWT 만료: userId={}, URI={}", e.getClaims().getSubject(), url);
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException e) {
            log.error("JWT 검증 실패 [{}]: URI={}", e.getClass().getSimpleName(), url, e);
            sendErrorResponse(response, HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다.");
        } catch (Exception e) {
            log.error("예상치 못한 오류: URI={}", url, e);
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "서버 처리 중 오류가 발생했습니다.");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("success", false);
        responseBody.put("message", message);
        responseBody.put("data", null);
        responseBody.put("timestamp", LocalDateTime.now());

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
