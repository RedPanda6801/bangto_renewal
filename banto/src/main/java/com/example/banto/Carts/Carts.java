package com.example.banto.Carts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;

@Data
@Builder
@RedisHash("cart")
@NoArgsConstructor
@AllArgsConstructor
public class Carts {
	@Id
	private String sessionId;

	HashMap<Long, CartItem> cartMap;
}
