package com.example.banto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.banto.DAOs.CommentDAO;
import com.example.banto.Comments.CommentDTO;
import com.example.banto.DTOs.ResponseDTO;

@Service
public class CommentService {
	@Autowired
	CommentDAO commentDAO;
	
	public void writeComment(CommentDTO dto, List<MultipartFile> files) throws Exception{
		try {
			commentDAO.writeComment(dto, files);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getItemComment(Integer itemId, Integer page) throws Exception{
		try {
			return commentDAO.getItemComment(itemId, page);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getComment(Integer commentId) throws Exception{
		try {
			return commentDAO.getComment(commentId);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getMyComment(Integer page) throws Exception{
		try {
			return commentDAO.getMyComment(page);
		}catch(Exception e) {
			throw e;
		}
	}

	// 관리자 + 본인 삭제 가능
	public void deleteComment(CommentDTO dto) throws Exception{
		try {
			if(dto.getId() == null){
				throw new Exception("필수 정보 오류");
			}
			commentDAO.deleteComment(dto);
		}catch(Exception e) {
			throw e;
		}
	}
}
