package com.example.banto.Users;

import com.example.banto.Authentications.AuthService;
import com.example.banto.Comments.Comments;
import com.example.banto.Exceptions.DuplicateUserException;
import com.example.banto.Exceptions.InvalidCredentialsException;
import com.example.banto.Exceptions.TokenCreationException;
import com.example.banto.Exceptions.UserNotFoundException;
import com.example.banto.JWTs.JwtUtil;
import com.example.banto.Qnas.QNAs;
import com.example.banto.SoldItems.SoldItems;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.banto.DTOs.ResponseDTO;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final AuthService authService;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public void sign(UserDTO dto){
		// 1. User 존재 여부 확인 -> 존재 시 예외 처리
		userRepository.findByEmail(dto.getEmail()).ifPresent(e -> {
			throw new DuplicateUserException("이미 존재하는 이메일입니다.");
		});
		if(!dto.getSnsAuth()) {
			// 2. SNS 여부 확인
			// 2-1. SNS 계정이 아니면 pw 값 암호화 후 dto에 다시 set
			dto.setSnsAuth(false);
			dto.setPw(passwordEncoder.encode(dto.getPw()));
		}
		else {
			// 2-2. SNS 계정 시 snsAuth(true)
			dto.setSnsAuth(true);
		}
		// 3. DTO -> Entity (메소드 내에서 기본 값 생성)
		Users user = Users.toEntity(dto);
		// 4. Repository 저장
		userRepository.save(user);
	}

	public String login(LoginDTO dto) {
		// 1. email 검색
		Users user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() ->   // 안티패턴 제거
			new InvalidCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.")
		);
		// 2. SNS 계정인지 확인
		//if(user.getSnsAuth()){}
		// 3. 비밀번호 일치 확인
		if(passwordEncoder.matches(dto.getPw(), user.getPw())){
			throw new InvalidCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		// 4. 토큰 반환
		try{
			return jwtUtil.generateToken(user.getId());
		}catch(Exception e){
			throw new TokenCreationException("토큰 발급에 실패했습니다.");
		}
	}

	public UserDTO getUser() {
		// 1. 유저 정보 조회 (AuthService에 로그인 및 권한 정보 확인 위임)
		Users user = authService.authToUser(SecurityContextHolder.getContext().getAuthentication());
		// 2. DTO 매핑 후 반환
		return UserDTO.toDTO(user);
	}

	public UserDTO getUserForAdmin(Long userId) {
		// 1. 어드민 권한 확인
		authService.authToAdmin(SecurityContextHolder.getContext().getAuthentication());
		// 2. 유저 정보 반환
		Users user = userRepository.findById(userId).orElseThrow(()->
			new UserNotFoundException("조회할 유저가 없습니다.")
		);
		// 3. pw 제거 후 DTO 반환
		return UserDTO.toDTO(user);
	}

	public ArrayList<UserDTO> getUserListForAdmin(Integer page) {
		// 1. 어드민 권한 확인
		authService.authToAdmin(SecurityContextHolder.getContext().getAuthentication());
		// 2. 페이지 객체 생성
		Pageable pageable = PageRequest.of(page, 20);
		// 3. 페이지 개수에 따른 유저 리스트 반환
		Page<Users> userPages = userRepository.findAll(pageable);
		// 4. 비어있으면 빈 객체 반환
		if(userPages.isEmpty()) {
			return new ArrayList<>();
		}
		else {
			// 5. 값이 있으면 DTO에 매핑 후 반환
			return userPages.stream().map(user -> {
				try{
					return UserDTO.toDTO(user);
				}catch(Exception e){
					// 변환 실패에 대한 예외 로그 처리 필요
					return null;
				}
			}).collect(Collectors.toCollection(ArrayList::new));
		}
	}

	@Transactional
	public void update(UserDTO dto) {
		// 1. 유저 권한 및 정보 조회
		Users user = authService.authToUser(SecurityContextHolder.getContext().getAuthentication());

		// 2. 수정 전 정보 확인
		user.setEmail((dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) ?
			dto.getEmail() : user.getEmail());
		user.setName((dto.getName() != null && !dto.getName().trim().isEmpty()) ?
			dto.getName() : user.getName());
		user.setPw((dto.getPw() != null && !dto.getPw().trim().isEmpty()) ?
			passwordEncoder.encode(dto.getPw()) : user.getPw());
		user.setPhone((dto.getPhone() != null && !dto.getPhone().trim().isEmpty()) ?
			dto.getPhone() : user.getPhone());
		user.setAddr((dto.getAddr() != null && !dto.getAddr().trim().isEmpty()) ?
			dto.getAddr() : user.getAddr());
	}

	@Transactional
	public void delete() throws Exception {
		// 1. 유저 본인 확인
		Users user = authService.authToUser(SecurityContextHolder.getContext().getAuthentication());
		// 2. User 관련 DB 처리
		// 2-1. 결제 물건 내역의 유저 null 처리

		// 2-2. 결제 내역의 유저 null 처리

		// 2-3. Comment의 유저 null 처리

		// 2-4. QNA의 유저 null 처리

		// 3. Seller 관련 DB 처리
		// 3-1. SellerAuth 조회

		// 3-2. SellerAuth 삭제

		// 3-3. Seller 여부 확인

		// 3-4. Seller -> Stores -> Items 조회

		// 3-5. 역순으로 삭제 및 연관 관계 삭제

		// 4. 유저 삭제


		List<SoldItems> soldItems = payRepository.findAllByUserId(user.getId());
		for(SoldItems payment : soldItems){
			payment.setUser(null);
			payRepository.save(payment);
		}
		Pageable pageable = Pageable.unpaged();
		// 후기 전부 null 처리
		Page<Comments> comments = commentRepository.findCommentsByUserId(user.getId(), pageable);
		for(Comments comment : comments){
			comment.setUser(null);
			commentRepository.save(comment);
		}
		// QNA 전부 null 처리
		Page<QNAs> qnas = qnaRepository.findAllByUserId(user.getId(), pageable);
		for(QNAs qna : qnas){
			qna.setUser(null);
			qnaRepository.save(qna);
		}

		// 추후에 다른 항목들도 삭제해야 될 수도 있음.
		userRepository.delete(user);
	}

	public void modifyUserForRoot(Integer userId, UserDTO dto) throws Exception {
		try {
				userDAO.modifyUserForRoot(userId, dto);
		}catch(Exception e) {
			throw e;
		}
	}

	public void deleteUser(Integer userId) throws Exception {
		try {
			userDAO.deleteUser(userId);
		}catch(Exception e) {
			throw e;
		}
	}
	public ResponseDTO isSnsSigned(String email) throws Exception {
		try {
			return userDAO.isSnsSigned(email);
		}catch(Exception e) {
			throw e;
		}
	}
}
