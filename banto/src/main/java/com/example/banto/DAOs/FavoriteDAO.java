package com.example.banto.DAOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.banto.DTOs.FavoriteDTO;
import com.example.banto.Items.ItemDTO;
import com.example.banto.DTOs.PageDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.Entitys.Favorites;
import com.example.banto.Items.Items;
import com.example.banto.Users.Users;
import com.example.banto.Repositorys.FavoriteRepository;
import com.example.banto.Repositorys.ItemRepository;

import jakarta.transaction.Transactional;

@Component
public class FavoriteDAO {
	@Autowired
	AuthDAO authDAO;
	@Autowired
	FavoriteRepository favoriteRepository;
	@Autowired
	ItemRepository itemRepository;
	
	@Transactional
	public void addFavorite(ItemDTO dto) throws Exception{
		try {
			// 인증 유효 확인
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			Optional<Items> itemOpt = itemRepository.findById(dto.getId());
			if(itemOpt.isEmpty()) {
				throw new Exception("아이템 정보 오류");
			}
			Optional<Favorites> favoriteOpt = favoriteRepository.findByUserAndItem(user.getId(), dto.getId());
			if(favoriteOpt.isEmpty()) {
				Favorites favorite = new Favorites(user, itemOpt.get());
				favoriteRepository.save(favorite);
			}
			// 있으면 아무것도 하지 않음
		}catch(Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public void deleteFavorite(ItemDTO dto) throws Exception{
		try {
			// 인증 유효 확인
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			Optional<Items> itemOpt = itemRepository.findById(dto.getId());
			if(itemOpt.isEmpty()) {
				throw new Exception("아이템 정보 오류");
			}
			Optional<Favorites> favoriteOpt = favoriteRepository.findByUserAndItem(user.getId(), dto.getId());
			if(favoriteOpt.isEmpty()) {
				throw new Exception("찜 없음");
			}
			else {
				favoriteRepository.delete(favoriteOpt.get());
			}
			// 있으면 아무것도 하지 않음
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getAllFavorites(Integer page) throws Exception{
		try {
			// 인증 유효 확인
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			Pageable pageable = PageRequest.of(page-1, 20, Sort.by("id").ascending());
			Page<Favorites> favoriteList = favoriteRepository.findAllByUserId(user.getId(), pageable);
			if(favoriteList.getSize() == 0) {
				throw new Exception("찜 없음");
			}
			else {
				List<FavoriteDTO> dtos = new ArrayList<>();
				for(Favorites favorite : favoriteList) {
					FavoriteDTO dto = FavoriteDTO.toDTO(favorite);
					dtos.add(dto);
				}
				return new ResponseDTO(dtos, new PageDTO(favoriteList.getSize(), favoriteList.getTotalElements(), favoriteList.getTotalPages()));
			}
			// 있으면 아무것도 하지 않음
		}catch(Exception e) {
			throw e;
		}
	}
}
