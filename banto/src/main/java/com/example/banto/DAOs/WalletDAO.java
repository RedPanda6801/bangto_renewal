package com.example.banto.DAOs;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.Users.Users;
import com.example.banto.Users.UserRepository;
import com.example.banto.Repositorys.WalletRepository;

import jakarta.transaction.Transactional;

@Component
public class WalletDAO {
	@Autowired
	WalletRepository walletRepository;
	@Autowired
	UserRepository	userRepository;
	@Autowired
	AuthDAO authDAO;
	
	public ResponseDTO findWallet() throws Exception{
		try {
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());

			Optional<Wallets>walletOpt =  walletRepository.findByUser_Id(user.getId());
			if(walletOpt.isEmpty()) {
				throw new Exception("지갑이 연결되어 있지 않음");
			}
			else {
				Wallets wallet = walletOpt.get();
				return new ResponseDTO(WalletDTO.toDTO(wallet), null);
			}
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO findWalletForRoot(Integer userId) throws Exception{
		try {
			if(!authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("관리자 권한 오류");
			}
			Optional<Users> userOpt = userRepository.findById(userId);
			if(userOpt.isEmpty()){
				throw new Exception("유저 정보 오류");
			}
			Optional<Wallets>walletOpt =  walletRepository.findByUser_Id(userOpt.get().getId());
			if(walletOpt.isEmpty()) {
				throw new Exception("지갑이 연결되어 있지 않음");
			}
			else {
				Wallets wallet = walletOpt.get();
				return new ResponseDTO(WalletDTO.toDTO(wallet), null);
			}
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	public void chargeMyWallet(WalletDTO dto) throws Exception{
		try {
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());

			if(dto.getCash() == null) {
				throw new Exception("수전할 캐시가 없습니다.");
			}
			Optional<Wallets> walletOpt = walletRepository.findByUser_Id(user.getId());
			if(walletOpt.isEmpty()) {
				throw new Exception("충전할 지갑이 없습니다.");
			}
			Wallets wallet = walletOpt.get();
			wallet.setCash(wallet.getCash() + dto.getCash());
			walletRepository.save(wallet);
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	public void modifyWallet(WalletDTO dto) throws Exception{
		try {
			// 인증 유효 확인
			if(!authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("관리자 권한 오류");
			}
			if(dto.getWalletPk() == null) {
				throw new Exception("수정할 지갑 Pk를 입력해주세요.");
			}
			else if(dto.getCash() == null && dto.getCashBack() == null) {
				throw new Exception("수정할 내용을 입력해주세요.");
			}
			else {
				Optional<Wallets> walletOpt = walletRepository.findById(dto.getWalletPk());
				if(walletOpt.isEmpty()) {
					throw new Exception("수정할 지갑이 없습니다.");
				}
				Wallets wallet = walletOpt.get();
				if(dto.getCash() != null) {
					wallet.setCash(dto.getCash());
				}
				if(dto.getCashBack() != null) {
					wallet.setCashBack(dto.getCashBack());
				}
				walletRepository.save(wallet);
			}
		}catch(Exception e) {
			throw e;
		}
	}
}
