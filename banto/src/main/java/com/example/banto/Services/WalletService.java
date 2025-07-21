package com.example.banto.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banto.DAOs.WalletDAO;
import com.example.banto.DTOs.ResponseDTO;

@Service
public class WalletService {
	@Autowired
	WalletDAO walletDAO;
	
	public ResponseDTO getMyWallet() throws Exception{
		try {
			return walletDAO.findWallet();
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO getMyWalletForRoot(Integer userId) throws Exception{
		try {
			return walletDAO.findWalletForRoot(userId);
		}catch(Exception e) {
			throw e;
		}
	}

	public void chargeMyWallet(WalletDTO dto) throws Exception{
		try {
			walletDAO.chargeMyWallet(dto);
		}catch(Exception e) {
			throw e;
		}
	}

	public void modifyWallet(WalletDTO dto) throws Exception{
		try {
			if(dto.getUserPk() == null){
				throw new Exception("수정할 유저 없음");
			}
			walletDAO.modifyWallet(dto);
		}catch(Exception e) {
			throw e;
		}
	}
}
