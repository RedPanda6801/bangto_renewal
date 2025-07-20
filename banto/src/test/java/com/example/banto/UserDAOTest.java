package com.example.banto;



import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.banto.Entitys.Users;
import com.example.banto.Repositorys.UserRepository;

import jakarta.transaction.Transactional;


@SpringBootTest
class UserDAOTest {

	@Autowired
	UserRepository userRepository;
	
	
	@Test
	@Transactional
	void test() {
		String email = "root@root";
		Optional<Users> userOpt = userRepository.findByEmail(email);

		Assertions.assertThat(userOpt).isEmpty();
	}

}
