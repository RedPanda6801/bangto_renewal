package com.example.banto.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.banto.DTOs.FavoriteDTO;
import com.example.banto.DTOs.ItemDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.JWTs.JwtUtil;
import com.example.banto.Repositorys.FavoriteRepository;
import com.example.banto.Services.FavoriteService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class FavoriteController {
	@Autowired
	FavoriteService favoriteService;
	
	@GetMapping("/favorite/get-list/{page}")
	public ResponseEntity getItemList(@PathVariable("page") Integer page) throws Exception{
		try {
			ResponseDTO itemList = favoriteService.getAllFavorites(page);
			return ResponseEntity.ok().body(itemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/favorite/add")
	public ResponseEntity addFavorites(@RequestBody ItemDTO itemDTO) throws Exception{
		try {
			favoriteService.addFavorite(itemDTO);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/favorite/delete")
	public ResponseEntity deleteFavorites(@RequestBody ItemDTO itemDTO) throws Exception{
		try {
			favoriteService.deleteFavorite(itemDTO);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
