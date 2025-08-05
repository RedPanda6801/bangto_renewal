package com.example.banto.JWTs;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@RedisHash(value = "BT", timeToLive = 900) // TTL : 15분
public class BlacklistToken {
    @Id
    private String JwtToken;

    private String status;
}
