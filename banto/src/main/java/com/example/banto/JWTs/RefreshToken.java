package com.example.banto.JWTs;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@RedisHash(value = "RT", timeToLive = 604800)   // 7일
public class RefreshToken {
    @Id
    private Long userId;

    private String jwtToken;

    public static RefreshToken toRedis(Long userId, String token){
        return RefreshToken.builder()
            .userId(userId)
            .jwtToken(token)
            .build();
    }
}
