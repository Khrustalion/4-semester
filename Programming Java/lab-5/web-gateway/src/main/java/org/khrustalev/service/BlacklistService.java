package org.khrustalev.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class BlacklistService {
    private final Set<String> blacklist = new ConcurrentSkipListSet<>();

    public void revoke(String jti) {
        blacklist.add(jti);
    }

    public boolean isRevoked(String jti) {
        return blacklist.contains(jti);
    }
}
