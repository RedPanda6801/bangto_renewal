package com.example.banto.Boards.Comments;

import java.util.List;

import com.example.banto.Authentications.AuthService;
import com.example.banto.Boards.BoardService;
import com.example.banto.Utils.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CommentService implements BoardService<CommentDTO, Long> {

	private final CommentRepository commentRepository;
	private final AuthService authService;
	/*
	@Override
	public void create(CommentDTO dto, List<MultipartFile> files) {
		// 인증 유효 확인
		Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
		Integer soldItemPk = dto.getSoldItemPk();
		Optional<SoldItems> soldItemOpt = payRepository.findById(soldItemPk);
		if(soldItemOpt.isEmpty()) {
			throw new Exception("결제된 적 없는 물품입니다.");
		}
		SoldItems soldItem = soldItemOpt.get();
		if(soldItem.getDeliverInfo() != DeliverType.Delivered) {
			throw new Exception("배송 완료된 물품만 후기를 작성할 수 있습니다.");
		}
		Integer itemPk = soldItem.getItemPk();
		Optional<Items> itemOpt = itemRepository.findById(itemPk);
		if(itemOpt.isEmpty()) {
			throw new Exception("존재하지 않는 물품입니다.");
		}
		if(dto.getContent() == null) {
			throw new Exception("후기 내용을 입력해주세요.");
		}
		if(dto.getStar() == null) {
			throw new Exception("별점을 입력해주세요.");
		}
		if(dto.getStar() < 1 || dto.getStar() > 5) {
			throw new Exception("별점은 1점 이상 5점 이하여야 합니다.");
		}
		// 프로젝트의 프론트엔드 경로
		String savePath = envConfig.get("FRONTEND_UPLOAD_ADDRESS");
		String img = "";
		if(files != null) {
			for(MultipartFile file : files) {
				String originalfilename = file.getOriginalFilename();
				String before = originalfilename.substring(0, originalfilename.indexOf("."));
				String ext = originalfilename.substring(originalfilename.indexOf("."));
				String newfilename = before + "(" + UUID.randomUUID() + ")" + ext;
				file.transferTo(new java.io.File(savePath + newfilename));
				img += newfilename + "/";
			}
			img = img.substring(0, img.length() - 1);
		}
		Comments comment = new Comments();
		comment.setContent(dto.getContent());
		comment.setImg(img);
		comment.setItem(itemOpt.get());
		comment.setStar(dto.getStar());
		comment.setUser(user);
		commentRepository.save(comment);
	}
	
	public ResponseDTO getItemComment(Integer itemId, Integer page) throws Exception{
		try {
			Pageable pageable = PageRequest.of(page-1, 20, Sort.by("writeDate").descending());
			Page<Comments> comments = commentRepository.findCommentsByItemId(itemId, pageable);
			List<CommentDTO> commentList = new ArrayList<CommentDTO>();
			for(Comments comment : comments) {
				CommentDTO dto = CommentDTO.toDTO(comment);
				commentList.add(dto);
			}

			return new ResponseDTO(commentList, new PageDTO(comments.getSize(), comments.getTotalElements(), comments.getTotalPages()));
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getComment(Integer commentId) throws Exception{
		try {
			Optional<Comments> commentOpt = commentRepository.findById(commentId);
			if(commentOpt.isEmpty()) {
				throw new Exception("존재하지 않는 후기입니다.");
			}
			else {
				Comments comment = commentOpt.get();
				CommentDTO dto = CommentDTO.toDTO(comment);
				return new ResponseDTO(dto, null);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getMyComment(Integer page) throws Exception{
		try {
			// 인증 유효 확인
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			Pageable pageable = PageRequest.of(page-1, 20, Sort.by("id").ascending());
			Page<Comments> comments = commentRepository.findCommentsByUserId(user.getId(), pageable);
			List<CommentDTO> commentList = new ArrayList<CommentDTO>();
			for(Comments comment : comments) {
				CommentDTO dto = CommentDTO.toDTO(comment);
				commentList.add(dto);
			}
			return new ResponseDTO(commentList, new PageDTO(comments.getSize(), comments.getTotalElements(), comments.getTotalPages()));
		}catch(Exception e) {
			throw e;
		}
	}

	// 관리자 + 본인 삭제 가능
	public void deleteComment(CommentDTO dto) {
		// 인증 유효 확인
		Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
		Optional<Comments> commentOpt = commentRepository.findById(dto.getId());
		if(commentOpt.isEmpty()){
			throw new Exception("후기 조회 오류");
		}else if(!authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())
			&& !commentOpt.get().getUser().getId().equals(user.getId())){
			throw new Exception("권한 오류");
		}
		Comments comment = commentOpt.get();
		commentRepository.delete(comment);
	}

	 */

	@Override
	public void create(CommentDTO dto, List<MultipartFile> files) {

	}

	@Override
	public PageDTO getList(int page) {
		return null;
	}

	@Override
	public CommentDTO getDetail(Long aLong) {
		return null;
	}

	@Override
	public PageDTO getListByStore(Long storeId, int page) {
		return null;
	}

	@Override
	public PageDTO getListByItem(Long itemId, int page) {
		return null;
	}

	@Override
	public void delete(Long aLong) {

	}
}
