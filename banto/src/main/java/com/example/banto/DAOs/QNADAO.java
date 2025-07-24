package com.example.banto.DAOs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.banto.DTOs.PageDTO;
import com.example.banto.Qnas.QNADTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.Items.Items;
import com.example.banto.Qnas.QNAs;
import com.example.banto.Sellers.Sellers;
import com.example.banto.Stores.Stores;
import com.example.banto.Users.Users;
import com.example.banto.Items.ItemRepository;
import com.example.banto.Repositorys.QNARepository;
import com.example.banto.Sellers.SellerRepository;
import com.example.banto.Stores.StoreRepository;

import jakarta.transaction.Transactional;

@Component
public class QNADAO {
	@Autowired
	AuthDAO authDAO;
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	QNARepository qnaRepository;
	@Autowired
	SellerRepository sellerRepository;
	@Autowired
	StoreRepository storeRepository;

	public ResponseDTO getListByStore(QNADTO qnaDTO, Integer page) throws Exception{
		try {
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());

			Optional<Stores> storeOpt = storeRepository.findById(qnaDTO.getStorePk());
			if(storeOpt.isEmpty()) {
				throw new Exception("매장 정보 오류");
			}else if(!Objects.equals(storeOpt.get().getSeller().getUser().getId(), user.getId())) {
				throw new Exception("본인 매장 불일치");
			}else {
				Pageable pageable = PageRequest.of(page-1, 20, Sort.by("id").ascending());
				Page<QNAs> qnaList = qnaRepository.findAllByStore(qnaDTO.getStorePk(), pageable);
				List<QNADTO> dtos = new ArrayList<>();
				for(QNAs qna : qnaList) {
					System.out.println(qna);
					QNADTO dto = QNADTO.toDTO(qna);
					dtos.add(dto);
				}
				return new ResponseDTO(dtos, new PageDTO(qnaList.getSize(), qnaList.getTotalElements(), qnaList.getTotalPages()));
			}
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	public void addQNA(QNADTO qnaDTO) throws Exception{
		try {
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			
			Optional<Items> itemOpt = itemRepository.findById(qnaDTO.getItemPk()); 
			if(itemOpt.isEmpty()) {
				throw new Exception("물품 정보 오류");
			}
			else {
				Items item = itemOpt.get();
				if(Objects.equals(item.getStore().getSeller().getUser().getId(), user.getId())) {
					throw new Exception("판매자 본인 등록 불가");
				}
				else {
					System.out.println(user);
					QNAs qna = new QNAs();
					qna.setUser(user);
					qna.setQContent(qnaDTO.getQContent());
					qna.setItem(itemOpt.get());
					qnaRepository.save(qna);
				}
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public void addSellerAnswer(QNADTO qnaDTO) throws Exception{
		try {
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			Optional<QNAs> qnaOpt = qnaRepository.findById(qnaDTO.getId());
			Optional<Sellers> sellerOpt = sellerRepository.findByUser_Id(user.getId());
			if(qnaOpt.isEmpty()) {
				throw new Exception("QNA 정보 오류");
			}else if(sellerOpt.isEmpty()) {
				// 판매자만 가능
				throw new Exception("판매자 인증 오류");
			}
			else {
				Sellers seller = sellerOpt.get();
				QNAs qna = qnaOpt.get();
				if(qna.getItem().getStore().getSeller().getId() != seller.getId()) {
					throw new Exception("판매자 물품 불일치");
				}
		        // 현재 시간 가져오기
		        LocalDateTime now = LocalDateTime.now();  

				qna.setAContent(qnaDTO.getAContent());
				qna.setAWriteDate(now);
				qnaRepository.save(qna);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getMyList(Integer page) throws Exception{
		try {
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			Pageable pageable = PageRequest.of(page-1, 10, Sort.by("id").ascending());
			Page<QNAs> qnaList = qnaRepository.findAllByUserId(user.getId(),pageable);
			List<QNADTO> dtos = new ArrayList<>();
			for(QNAs qna : qnaList) {
				QNADTO dto = QNADTO.toDTO(qna);
				dtos.add(dto);
			}
			return new ResponseDTO(dtos, null);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getQnaDetail(QNADTO qnaDTO) throws Exception{
		try {
			Optional<QNAs> qnaOpt = qnaRepository.findById(qnaDTO.getId());
			if(qnaOpt.isEmpty()) {
				return null;
			}
			return new ResponseDTO(QNADTO.toDTO(qnaOpt.get()), null);
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	public void deleteQNA(QNADTO qnaDTO) throws Exception{
		try {
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());

			Optional<QNAs> qnaOpt = qnaRepository.findById(qnaDTO.getId());
			if(qnaOpt.isEmpty()) {
				throw new Exception("물품 정보 오류");
			} else if(!authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())
					&& !qnaOpt.get().getUser().getId().equals(user.getId())){
				throw new Exception("권한 오류");
			} else {
				QNAs qna = qnaOpt.get();
				qnaRepository.delete(qna);
			}
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO getListByItem(Integer itemId, Integer page) throws Exception{
		try {
			Pageable pageable = PageRequest.of(page-1, 10, Sort.by("id").ascending());
			Page<QNAs> qnaList = qnaRepository.findAllByItemId(itemId,pageable);
			List<QNADTO> dtos = new ArrayList<>();
			for(QNAs qna : qnaList) {
				QNADTO dto = QNADTO.toDTO(qna);
				dtos.add(dto);
			}
			return new ResponseDTO(dtos, new PageDTO(qnaList.getSize(), qnaList.getTotalElements(), qnaList.getTotalPages()));
		}catch(Exception e) {
			throw e;
		}
	}
}
