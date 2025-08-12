package com.example.banto.Boards.Qnas;

import com.example.banto.Utils.PageDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
//@RequiredArgsConstructor	-> Qualifier 사용 시 강한 객체 주입 필요
@RequestMapping("/api")
public class QNAController {
	private final QNAService qnaService;

	public QNAController(@Qualifier("qnaService") QNAService qnaService){
		this.qnaService = qnaService;
	}

	// QNA 추가(고객)
	@PostMapping(path = "/qna/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createQNA(@Valid @RequestBody QNADTO qnaDTO
	, List<MultipartFile> files) {
		qnaService.create(qnaDTO, files);
		return ResponseEntity.ok().body("질문 추가에 성공했습니다.");
	}

	// QNA 답변 추가(판매자)
	@PutMapping("/seller/qna/answer")
	public ResponseEntity<?> answerToQNA(@RequestBody QNADTO qnaDTO) {
		qnaService.answer(qnaDTO);
		return ResponseEntity.ok().body("답변이 추가되었습니다");
	}

	//	내 QNA 조회(고객)
	@GetMapping("/qna/get-list/{page}")
	public ResponseEntity<?> getQNAList(@PathVariable("page") Integer page) {
			PageDTO qnaList = qnaService.getList(page);
			return ResponseEntity.ok().body(qnaList);
	}

	// 매장 별 QNA 전체 조회(판매자)
	@GetMapping("/seller/qna/get-list/store/{storeId}/{page}")
	public ResponseEntity<?> getListByStore(@PathVariable("storeId") Long storeId, @PathVariable("page") Integer page) {
		PageDTO qnaList = qnaService.getListByStore(storeId, page);
		return ResponseEntity.ok().body(qnaList);
	}

	//	아이템별 QNA 조회
	@GetMapping("/qna/get-list/item/{itemId}/{page}")
	public ResponseEntity<?> getListByItem(@PathVariable("itemId") Long itemId, @PathVariable("page") Integer page) {
			PageDTO qnaList = qnaService.getListByItem(itemId, page);
			return ResponseEntity.ok().body(qnaList);
	}
		
	// QNA 세부 조회(판매자 or 고객)
	@GetMapping("/qna/{qnaId}")
	public ResponseEntity<?> getQnaDetail(@PathVariable("qnaId") Long qnaId){
		QNADTO itemList = qnaService.getDetail(qnaId);
		return ResponseEntity.ok().body(itemList);
	}

	// QNA 삭제
	@DeleteMapping("/qna/delete/{qnaId}")
	public ResponseEntity<?> deleteQNA(@PathVariable("qnaId") Long qnaId) {
		qnaService.delete(qnaId);
		return ResponseEntity.ok().body("삭제에 성공했습니다");
	}
}
