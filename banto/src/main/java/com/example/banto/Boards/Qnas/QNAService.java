package com.example.banto.Boards.Qnas;

import com.example.banto.Authentications.AuthService;
import com.example.banto.Boards.BoardService;
import com.example.banto.Configs.EnvConfig;
import com.example.banto.Utils.PageDTO;
import com.example.banto.Exceptions.ForbiddenException;
import com.example.banto.Exceptions.ResourceNotFoundException;
import com.example.banto.Exceptions.ValidationException;
import com.example.banto.Items.ItemRepository;
import com.example.banto.Options.OptionRepository;
import com.example.banto.Options.Options;
import com.example.banto.Sellers.SellerRepository;
import com.example.banto.Sellers.Sellers;
import com.example.banto.Stores.StoreRepository;
import com.example.banto.Stores.Stores;
import com.example.banto.Users.Users;
import com.example.banto.Utils.DTOMapper;
import com.example.banto.Utils.ImageHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QNAService implements BoardService<QNADTO, Long> {

	private final QNARepository qnaRepository;
	private final OptionRepository optionRepository;
	private final QNAImagesRepository qnaImagesRepository;
	private final SellerRepository sellerRepository;
	private final StoreRepository storeRepository;
	private final ItemRepository itemRepository;
	private final AuthService authService;
	private final EnvConfig envConfig;

	@Override
	@Transactional
	public void create(QNADTO qnaDTO, List<MultipartFile> files) {
		// 1. 로그인 정보 및 권한 확인
		Users user = authService.authToUser(SecurityContextHolder.getContext().getAuthentication());
		// 2. Option 조회
		Options option = optionRepository.findById(qnaDTO.getOptionPk())
			.orElseThrow(() -> new ResourceNotFoundException("옵션 정보가 없습니다."));
		// 3. 물건 판매자와 질문자 일치 여부 확인
		if(Objects.equals(user.getId(), option.getItem().getStore().getSeller().getUser().getId())){
			throw new ForbiddenException("판매자 본인은 질문할 수 없습니다.");
		}
		// 4. QNA 추가 로직
		QNAs qna = QNAs.toEntity(user, option, qnaDTO.getQContent());
		qnaRepository.save(qna);
		// 5. 이미지 추가
		for (MultipartFile file : files){
			String newFileName = ImageHandler.imageMapper(file, envConfig.get("FRONTEND_UPLOAD_ADDRESS"));
			QNAImages qnaImages = QNAImages.toEntity(qna, newFileName);
			qnaImagesRepository.save(qnaImages);
		}
	}

	@Transactional
	public void answer(QNADTO qnaDTO) {
		// 1. 판매자 권한 확인
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		authService.authToSeller(authentication);
		// 2. 판매자 및 QNA 조회
		Sellers seller = sellerRepository.findByUserId(
			Long.parseLong(authentication.getName()))
			.orElseThrow(() -> new ResourceNotFoundException("판매자가 없습니다."));
		QNAs qna = qnaRepository.findById(qnaDTO.getId())
			.orElseThrow(() -> new ResourceNotFoundException("질문 정보가 없습니다."));
		// 3. 판매자가 qna 옵션의 판매자와 일치하는지 확인
		if(!Objects.equals(qna.getOption().getItem().getStore().getSeller().getId(), seller.getId())){
			throw new ForbiddenException("답변할 권한이 없습니다.");
		}
		// 4. 답변 확인 후 추가
		if(qnaDTO.getAContent() == null || qnaDTO.getAContent().trim().isEmpty()){
			throw new ValidationException("답변 정보가 없습니다.");
		}
		qna.setAContent(qnaDTO.getAContent());
		qna.setAWriteDate(LocalDateTime.now());
	}

	@Override
	public PageDTO getList(int page) {
		// 1. 본인 인증
		Users user = authService.authToUser(SecurityContextHolder.getContext().getAuthentication());
		// 2. 페이징 객체 생성
		Pageable pageable = PageRequest.of(page, 20, Sort.by("qWriteDate").descending());
		// 3. 내 qna 조회
		Page<QNAs> qnaPages = qnaRepository.findAllByUserId(user.getId(),pageable);
		// 4. 빈값 처리
		if(qnaPages.isEmpty()){
			return new PageDTO(new ArrayList<>(), qnaPages.getTotalPages());
		}
		// 5. entity -> dto
		List<QNADTO> qnaList = DTOMapper.convertList(qnaPages.stream(), QNADTO::toDTO);
		// 6. Page 객체로 반환
		return new PageDTO(qnaList, qnaPages.getTotalPages());
	}

	@Override
	public PageDTO getListByStore(Long storeId, int page){
		// 1. 판매자 권한 인증
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		authService.authToSeller(authentication);
		// 2. 매장 조회
		Stores store = storeRepository.findById(storeId)
			.orElseThrow(() -> new ResourceNotFoundException("매장 정보가 없습니다."));
		// 3. 매장과 판매자 일치 여부 확인
		if(!Objects.equals(store.getSeller().getUser().getId(), Long.parseLong(authentication.getName()))){
			throw new ForbiddenException("매장에 대한 권한이 없습니다.");
		}
		// 4. 페이지 객체 생성 (질문 날짜 순 조회)
		Pageable pageable = PageRequest.of(page, 20, Sort.by("qWriteDate").descending());
		// 5. QNA 조회
		Page<QNAs> qnaPages = qnaRepository.findAllByStoreId(storeId, pageable);
		// 6. 빈값 예외 처리
		if(qnaPages.isEmpty()){
			return new PageDTO(new ArrayList<>(), qnaPages.getTotalPages());
		}
		// 7. Entity -> DTO
		List<QNADTO> qnaList = DTOMapper.convertList(qnaPages.stream(), QNADTO::toDTO);
		// 8. Page 객체로 반환
		return new PageDTO(qnaList, qnaPages.getTotalPages());
	}

	@Override
	public PageDTO getListByItem(Long itemId, int page){
		// 1. 페이징 객체 생성
		Pageable pageable = PageRequest.of(page, 20);
		// 2. itemId로 QNA 조회
		Page<QNAs> qnaPages = qnaRepository.findAllByItemId(itemId, pageable);
		// 3. 빈값 예외 처리
		if(qnaPages.isEmpty()){
			return new PageDTO(new ArrayList<>(), qnaPages.getTotalPages());
		}
		// 4. Entity -> DTO
		List<QNADTO> qnaList = DTOMapper.convertList(qnaPages.stream(), QNADTO::toDTO);
		// 8. Page 객체로 반환
		return new PageDTO(qnaList, qnaPages.getTotalPages());
	}

	@Override
	public QNADTO getDetail(Long id) {
		// 1. 권한 확인
		Users user = authService.authToUser(SecurityContextHolder.getContext().getAuthentication());
		// 2. qna 조회
		QNAs qna = qnaRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("질문 정보가 없습니다."));
		// 3. 권한 확인(본인 또는 판매자)
		// 3-1. 본인 확인
		if(!Objects.equals(qna.getUser().getId(), user.getId())){
			// 3-2. 유저가 판매자가 아니거나 판매자여도 qna 물건의 판매자가 아니면 예외 처리
			if(user.getSellers() == null ||
				!Objects.equals(user.getSellers().getId(), qna.getOption().getItem().getStore().getSeller().getId())){
				throw new ForbiddenException("질문에 접근할 권한이 없습니다.");
			}
		}
		// 4. Entity -> DTO 반환
		return QNADTO.toDTO(qna);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		// 1. 본인 권한 확인
		Users user = authService.authToUser(SecurityContextHolder.getContext().getAuthentication());
		// 2. 삭제할 qna 조회
		QNAs qna  = qnaRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("삭제할 질문이 없습니다."));
		// 3. 질문자 본인 확인
		if(!Objects.equals(user.getId(), qna.getUser().getId())){
			throw new ForbiddenException("삭제할 권한이 없습니다.");
		}
		// 4. 삭제 로직
		qnaRepository.delete(qna);
	}
}
