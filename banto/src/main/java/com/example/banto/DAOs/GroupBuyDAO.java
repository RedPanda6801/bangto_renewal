package com.example.banto.DAOs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.banto.DTOs.GroupBuyDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.Entitys.GroupBuys;
import com.example.banto.Entitys.Users;
import com.example.banto.Repositorys.GroupBuyRepository;

import jakarta.transaction.Transactional;

@Component
public class GroupBuyDAO {
	@Autowired
	AuthDAO authDAO;
	@Autowired
	GroupBuyRepository groupBuyRepository;
	
	// 날짜 추가 (관리자)
	@Transactional
	public void addEvent(GroupBuyDTO dto) throws Exception{
		try {
			if(!authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("관리자 권한 오류");
			}
			// 시작 날짜 조회 후 날짜 중복 확인
			Optional<GroupBuys> event = groupBuyRepository.findLatest();
			// 시작 날짜가 가장 최근에 추가 했던 이벤트 마감일보다 빠르면 예외 처리
			if(event.isPresent() && dto.getStartDate().isBefore(event.get().getEndDate())) {
				throw new Exception("날짜 정보 오류");
			}else {
				// 날짜 추가 로직
				groupBuyRepository.save(GroupBuys.toEntity(dto));
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getEventList() throws Exception{
		try {
			if(authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication()) == -1 &&
					!authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("권한 오류");
			}
			List<GroupBuys> eventList = groupBuyRepository.findAll();
			// 시작 날짜가 가장 최근에 했던 이벤트 마감일보다 빠르면 예외 처리
			List<GroupBuyDTO> dtos = new ArrayList<>();
			for(GroupBuys event : eventList) {
				dtos.add(GroupBuyDTO.toDTO(event));
			}
			return new ResponseDTO(dtos, null);
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO getChooseList() throws Exception {
		try {
			if(authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication()) == -1){
				throw new Exception("권한 오류");
			}
			LocalDateTime currentDate = LocalDateTime.now();
			List<GroupBuys> eventList = groupBuyRepository.findAbleEvent(currentDate);
			// 시작 날짜가 가장 최근에 했던 이벤트 마감일보다 빠르면 예외 처리
			List<GroupBuyDTO> dtos = new ArrayList<>();
			for(GroupBuys event : eventList) {
				dtos.add(GroupBuyDTO.toDTO(event));
			}
			return new ResponseDTO(dtos, null);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getCurrentEvent() throws Exception{
		try {
			LocalDateTime currentDate = LocalDateTime.now();

			Optional<GroupBuys> eventOpt =
					groupBuyRepository.findCurrentEvent(currentDate);
			// 시작 날짜가 가장 최근에 했던 이벤트 마감일보다 빠르면 예외 처리
			if(eventOpt.isEmpty()) {
				throw new Exception("존재하는 이벤트 없음");
			}
			return new ResponseDTO(GroupBuyDTO.toDTO(eventOpt.get()), null);
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO getEventListToSeller() throws Exception{
		try {
			int sellerId = authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication());
			if(sellerId == -1 && !authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("권한 오류");
			}
			List<GroupBuys> eventList = groupBuyRepository.findAllBySellerPk(sellerId);
			// 시작 날짜가 가장 최근에 했던 이벤트 마감일보다 빠르면 예외 처리
			List<GroupBuyDTO> dtos = new ArrayList<>();
			for(GroupBuys event : eventList) {
				dtos.add(GroupBuyDTO.toDTO(event));
			}
			return new ResponseDTO(dtos, null);
		}catch(Exception e) {
			throw e;
		}
	}

	// 날짜 추가 (관리자)
	@Transactional
	public void deleteEvent(GroupBuyDTO dto) throws Exception{
		try {
			if(!authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("관리자 권한 오류");
			}
			// 시작 날짜 조회 후 날짜 중복 확인
			Optional<GroupBuys> event = groupBuyRepository.findById(dto.getId());
			if(event.isEmpty()){
				throw new Exception("이벤트 조회 오류");
			}
			// 시작 날짜가 가장 최근에 추가 했던 이벤트 마감일보다 빠르면 예외 처리
			groupBuyRepository.delete(event.get());
		}catch(Exception e) {
			throw e;
		}
	}
}
