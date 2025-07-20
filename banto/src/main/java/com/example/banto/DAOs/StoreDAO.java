package com.example.banto.DAOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.banto.Entitys.*;
import com.example.banto.Repositorys.GroupBuyPayRepository;
import com.example.banto.Repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.banto.Configs.EnvConfig;
import com.example.banto.DTOs.PageDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.DTOs.SellerDTO;
import com.example.banto.DTOs.StoreDTO;
import com.example.banto.Repositorys.SellerRepository;
import com.example.banto.Repositorys.StoreRepository;

import jakarta.transaction.Transactional;

@Component
public class StoreDAO {
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	SellerRepository sellerRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	GroupBuyPayRepository groupBuyPayRepository;
	@Autowired
	AuthDAO authDAO;

	@Transactional
	public void create(StoreDTO dto) throws Exception {
		try {
			// 인증 유효 확인
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			// 판매자 가져오기
			Optional<Sellers> seller = sellerRepository.findByUser_Id(user.getId());
			if(seller.isEmpty()) {
				throw new Exception("판매자 권한 오류");
			}
			else {
				// 매장 추가
				Stores store = Stores.toEntity(dto);
				store.setSeller(seller.get());
				storeRepository.save(store);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getMyStores() throws Exception {
		try {
			// 인증 유효 확인
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			// 판매자 가져오기
			Optional<Sellers> sellerOpt = sellerRepository.findByUser_Id(user.getId());
			if(sellerOpt.isEmpty()){
				throw new Exception("판매자 정보 오류");
			}
			//판매자 pk로 store 전부 찾기
			List<StoreDTO> storeList = storeRepository.findAllBySellerId(sellerOpt.get().getId());
			return new ResponseDTO(storeList, null);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getStore(Integer storeId) throws Exception {
		try {
			// 인증 유효 확인
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			// 판매자 가져오기
			Optional<Sellers> sellerOpt = sellerRepository.findByUser_Id(user.getId());
			if(sellerOpt.isEmpty()){
				throw new Exception("판매자 정보 오류");
			}
			Sellers seller = sellerOpt.get();
			//판매자 pk로 store 전부 찾기
			Optional<Stores> storeOpt = storeRepository.findById(storeId);
			if(storeOpt.isEmpty()) {
				throw new Exception("매장 조회 오류");
			}
			else {
				Stores store = storeOpt.get();
				if((Objects.equals(seller.getId(), store.getSeller().getId()))) {
					// 필요한 데이터만 조회
					store.setItems(null);
					return new ResponseDTO(StoreDTO.toDTO(store), null);
				}
				else {
					throw new Exception("판매자 권한 오류");
				}
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public void modify(StoreDTO dto) throws Exception {
		try {
			// 인증 유효 확인
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			// 판매자 가져오기
			Optional<Stores> storeOpt = storeRepository.findStoreByUserId(user.getId(), dto.getId());
			if(storeOpt.isEmpty()) {
				throw new Exception("매장 없음");
			}
			else {
				Stores store = storeOpt.get();
				store.setBusiNum(
						(dto.getBusiNum() != null && !dto.getBusiNum().equals("")) ?
								dto.getBusiNum() : store.getBusiNum());
				store.setName((dto.getName() != null && !dto.getName().equals("")) ?
						dto.getName() : store.getName());
				storeRepository.save(store);
			}
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	public void delete(StoreDTO dto) throws Exception {
		try {
			// 인증 유효 확인
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			// 매장 가져오기
			Optional<Stores> storeOpt = storeRepository.findStoreByUserId(user.getId(), dto.getId());
			if(storeOpt.isEmpty()) {
				throw new Exception("매장 없음");
			}else if(!storeOpt.get().getSeller().getUser().getId().equals(user.getId())){
				throw new Exception("권한 없음");
			}
			else {
				Stores store = storeOpt.get();
				if(!store.getItems().isEmpty()){
					for(Items item : store.getItems()){
						List<GroupItemPays> payments = groupBuyPayRepository.findByItemId(item.getId());
						for(GroupItemPays payment : payments){
							payment.setItem(null);
							groupBuyPayRepository.save(payment);
						}
					}
				}
				storeRepository.delete(store);
			}
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	public void modifyForRoot(StoreDTO dto) throws Exception {
		try {
			// 인증 유효 확인
			if(!authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("관리자 권한 오류");
			}
			// 판매자 가져오기
			Optional<Stores> storeOpt = storeRepository.findById(dto.getId());
			if(storeOpt.isEmpty()) {
				throw new Exception("매장 없음");
			}
			else {
				Stores store = storeOpt.get();
				store.setBusiNum(
						(dto.getBusiNum() != null && !dto.getBusiNum().equals("")) ?
								dto.getBusiNum() : store.getBusiNum());
				store.setName((dto.getName() != null && !dto.getName().equals("")) ?
						dto.getName() : store.getName());
				storeRepository.save(store);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getMyStoresByRoot(Integer page) throws Exception {
		try {
			if(!authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("관리자 권한 오류");
			}
			// 10명씩 끊기
			Pageable pageable = PageRequest.of(page-1, 10, Sort.by("id").ascending());
			Page<Stores> storeListPage = storeRepository.findAll(pageable);
			List<StoreDTO>storeList = new ArrayList<>();
			for(Stores store : storeListPage) {
				storeList.add(StoreDTO.toDTO(store));
			}
			return new ResponseDTO(storeList, new PageDTO(storeListPage.getSize(), storeListPage.getTotalElements(), storeListPage.getTotalPages()));
		}catch(Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public void deleteByRoot(StoreDTO dto) throws Exception {
		try {
			// 인증 유효 확인
			if(!authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("관리자 권한 오류");
			}
			// 매장 가져오기
			Optional<Stores> storeOpt = storeRepository.findById(dto.getId());
			if(storeOpt.isEmpty()) {
				throw new Exception("매장 없음");
			}else {
				Stores store = storeOpt.get();
				if(!store.getItems().isEmpty()){
					for(Items item : store.getItems()){
						List<GroupItemPays> payments = groupBuyPayRepository.findByItemId(item.getId());
						for(GroupItemPays payment : payments){
							payment.setItem(null);
							groupBuyPayRepository.save(payment);
						}
					}
				}
				storeRepository.delete(store);
			}
		}catch(Exception e) {
			throw e;
		}
	}
}
