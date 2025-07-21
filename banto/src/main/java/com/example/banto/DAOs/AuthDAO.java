package com.example.banto.DAOs;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.banto.Users.Users;
import com.example.banto.Users.UserRepository;

@Component
public class AuthDAO {
	@Autowired
	UserRepository userRepository;

	public Users auth(Authentication authentication) throws Exception{
		try {
			int userId = Integer.parseInt(authentication.getName());
			Optional<Users> user = userRepository.findById(userId);
			if(user.isEmpty()){
				throw new Exception("유저 인증 오류");
			}
			else {
				return user.get();
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	public boolean authRoot(Authentication authentication) throws Exception {
		try {
			for(GrantedAuthority auth : authentication.getAuthorities()){
				if(auth.getAuthority().equals("ROLE_ADMIN") ||
						auth.getAuthority().equals("ADMIN")){
					return true;
				}
			}
			return false;
		}catch(Exception e) {
			throw e;
		}
	}
	public Integer authSeller(Authentication authentication) throws Exception {
		try {
			for(GrantedAuthority auth : authentication.getAuthorities()){
				if(auth.getAuthority().equals("ROLE_SELLER") ||
						auth.getAuthority().equals("SELLER")){
					return Integer.parseInt(authentication.getName());
				}
			}
			return -1;
		}catch(Exception e) {
			throw e;
		}
	}
}
