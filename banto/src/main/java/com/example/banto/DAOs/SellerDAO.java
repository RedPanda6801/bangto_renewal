package com.example.banto.DAOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.banto.DTOs.PageDTO;
import com.example.banto.Enums.ApplyType;
import com.example.banto.Items.Items;
import com.example.banto.Repositorys.*;
import com.example.banto.SellerAuths.SellerAuths;
import com.example.banto.Sellers.Sellers;
import com.example.banto.Stores.Stores;
import com.example.banto.Users.UserRepository;
import com.example.banto.Users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.Sellers.SellerDTO;

import jakarta.transaction.Transactional;

@Component
public class SellerDAO {
	@Autowired
	SellerRepository sellerRepository;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	GroupBuyPayRepository groupBuyPayRepository;
	@Autowired
	ApplyRepository applyRepository;
	@Autowired
	AuthDAO authDAO;
	@Autowired
	private UserRepository userRepository;

	public ResponseDTO findSeller() throws Exception {
		try {
			// 인증 유효 확인
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			Optional<Sellers>sellerOpt =  sellerRepository.findByUser_Id(user.getId());
			if(sellerOpt.isEmpty()) {
				throw new Exception("데이터 조회 오류");
			}
			else {
				Sellers seller = sellerOpt.get();
				return new ResponseDTO(SellerDTO.toDTO(seller), null);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public void deleteMyself() throws Exception{
		try {
			// 인증 유효 확인
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			Optional<Sellers> sellerOpt = sellerRepository.findByUser_Id(user.getId());
			if(sellerOpt.isEmpty()) {
				throw new Exception("판매자가 아닙니다.");
			}
			else {
				Sellers seller = sellerOpt.get();
				// 신청된 신청서에 대한 Ban 처리
				List<SellerAuths> applys = applyRepository.findAllByUserId(seller.getUser().getId());
				if(!applys.isEmpty()){
					for(SellerAuths apply : applys){
						if(apply.getAuth().equals(ApplyType.Accepted)){
							apply.setAuth(ApplyType.Banned);
							applyRepository.save(apply);
						}
					}
				}
				// 연관관계 삭제 로직
				List<Stores> stores = storeRepository.findAllBySellerIdToEntity(seller.getId());
				if(!stores.isEmpty()){
					for(Stores store : stores){
						if(!store.getItems().isEmpty()){
							for(Items item : store.getItems()){
								List<GroupItemPays> payments = groupBuyPayRepository.findByItemId(item.getId());
								for(GroupItemPays payment : payments){
									payment.setItem(null);
									groupBuyPayRepository.save(payment);
								}
							}
						}
					}
				}
				seller.setUser(null);
				user.setSellers(null);
				userRepository.save(user);
				sellerRepository.delete(seller);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public void deleteSeller(Integer userId) throws Exception{
		try {
			if(!authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("관리자 권한 오류");
			}
			Optional<Sellers>sellerOpt = sellerRepository.findByUser_Id(userId);
			Optional<Users> userOpt = userRepository.findById(userId);
			if(sellerOpt.isEmpty() || userOpt.isEmpty()) {
				throw new Exception("판매자가 아닙니다.");
			}
			else {
				Sellers seller = sellerOpt.get();
				Users user = userOpt.get();
				// 신청된 신청서에 대한 Ban 처리
				List<SellerAuths> applys = applyRepository.findAllByUserId(seller.getUser().getId());
				if(!applys.isEmpty()){
					for(SellerAuths apply : applys){
						if(apply.getAuth().equals(ApplyType.Accepted)){
							apply.setAuth(ApplyType.Banned);
							applyRepository.save(apply);
						}
					}
				}
				// 연관관계 삭제 로직
				List<Stores> stores = storeRepository.findAllBySellerIdToEntity(seller.getId());
				if(!stores.isEmpty()){
					for(Stores store : stores){
						if(!store.getItems().isEmpty()){
							for(Items item : store.getItems()){
								List<GroupItemPays> payments = groupBuyPayRepository.findByItemId(item.getId());
								for(GroupItemPays payment : payments){
									payment.setItem(null);
									groupBuyPayRepository.save(payment);
								}
							}
						}
					}
				}
				seller.setUser(null);
				user.setSellers(null);
				userRepository.save(user);
				sellerRepository.delete(seller);
			}
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO getSellerList(int page) throws Exception{
		try {
			if(!authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("관리자 권한 오류");
			}
			Pageable pageable = PageRequest.of(page-1, 20, Sort.by("id").ascending());
			Page<Sellers> sellerList = sellerRepository.findAll(pageable);
			if(sellerList.isEmpty()) {
				return new ResponseDTO(null, null);
			}
			else {
				List<SellerDTO> dtos = new ArrayList<>();
				for(Sellers seller : sellerList){
					dtos.add(SellerDTO.toDTO(seller));
				}
				return new ResponseDTO(dtos, new PageDTO(sellerList.getSize(), sellerList.getTotalElements(), sellerList.getTotalPages()));
			}
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO findSellerForRoot(int userId) throws Exception{
		try {
			if(!authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("관리자 권한 오류");
			}
			// 인증 유효 확인
			Optional<Sellers>sellerOpt =  sellerRepository.findByUser_Id(userId);
			if(sellerOpt.isEmpty()) {
				throw new Exception("데이터 조회 오류");
			}
			else {
				Sellers seller = sellerOpt.get();
				return new ResponseDTO(SellerDTO.toDTO(seller), null);
			}
		}catch(Exception e) {
			throw e;
		}
	}
}
