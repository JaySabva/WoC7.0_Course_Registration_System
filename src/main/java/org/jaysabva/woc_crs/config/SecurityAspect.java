package org.jaysabva.woc_crs.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.jaysabva.woc_crs.util.JwtProvider;
import org.jaysabva.woc_crs.util.Role;
import org.jaysabva.woc_crs.util.RoleRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class SecurityAspect {

    @Autowired
    private JwtProvider jwtProvider;

    @Around("execution(* org.jaysabva.woc_crs..*(..)) && @within(org.jaysabva.woc_crs.util.RoleRequired)")
    public Object checkRoleAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = getJwtFromRequest();

        if (token == null) {
            throw new UnauthorizedAccessException("Invalid or missing JWT token");
        }

        try {
            jwtProvider.validateToken(token);
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Invalid or expired JWT token");
        }

        // Extract user details (e.g., role) from the token
        Claims claims = jwtProvider.validateToken(token);
        String role = claims.get("role", String.class);

        // Retrieve the required roles from the class annotation
        RoleRequired roleRequired = joinPoint.getTarget().getClass().getAnnotation(RoleRequired.class);
        Role[] allowedRoles = roleRequired.value();

        // Check if the user's role is allowed to access the class and its methods
        if (!Arrays.stream(allowedRoles).anyMatch(r -> r.name().equals(role))) {
            throw new ForbiddenAccessException("You don't have the required role to access this resource");
        }

        // Proceed with the method execution if everything is valid
        return joinPoint.proceed();
    }

    private String getJwtFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}

