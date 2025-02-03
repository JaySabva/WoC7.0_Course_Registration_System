package org.jaysabva.woc_crs.config;

import io.jsonwebtoken.Claims;

import java.util.Map;

public class JwtContext {
    private static final ThreadLocal<Map<String, String>> currentJwtClaims = new ThreadLocal<>();

    public static void setJwtClaims(Map<String, String> idAndEmail) {
        currentJwtClaims.set(idAndEmail);
    }

    public static Long getId() {
        return Long.parseLong(currentJwtClaims.get().get("id"));
    }

    public static String getEmail() {
        return currentJwtClaims.get().get("email");
    }
    public static void clear() {
        currentJwtClaims.remove();
    }
}
