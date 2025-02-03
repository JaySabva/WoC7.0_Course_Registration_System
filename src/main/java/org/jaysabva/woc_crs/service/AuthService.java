package org.jaysabva.woc_crs.service;

import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    Object login(String username, String password);
}
