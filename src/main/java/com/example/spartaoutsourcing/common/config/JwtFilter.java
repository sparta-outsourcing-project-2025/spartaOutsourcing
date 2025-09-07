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
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

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

        HttpServletRequest reqToUse = request;
        if (!(request instanceof ContentCachingRequestWrapper)) {
            reqToUse = new ContentCachingRequestWrapper(request);
        }


        String url = reqToUse.getRequestURI();

        // /api/auth/register, /api/auth/login 은 검증 제외
        if (url.equals("/api/auth/register") || url.equals("/api/auth/login")) {
            filterChain.doFilter(reqToUse, response);
            return;
        }

        String bearerJwt = reqToUse.getHeader("Authorization");

        if (bearerJwt == null) {
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

            reqToUse.setAttribute("userId", Long.parseLong(claims.getSubject()));
            reqToUse.setAttribute("email", claims.get("email"));
            reqToUse.setAttribute("userRole", userRole);

            filterChain.doFilter(reqToUse, response);

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

        // AuditableEntity 기준 timestamp
        responseBody.put("timestamp", LocalDateTime.now()); // Auditable과 동일한 형식 사용

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
