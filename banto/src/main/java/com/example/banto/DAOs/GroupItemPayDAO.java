package com.example.banto.DAOs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.banto.Entitys.*;
import com.example.banto.Repositorys.*;
import com.example.banto.Utils.PayCulculator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.banto.DTOs.GroupBuyItemDTO;
import com.example.banto.DTOs.GroupItemPayDTO;
import com.example.banto.DTOs.ResponseDTO;

@Component
public class GroupItemPayDAO {
	@Autowired
	AuthDAO authDAO;

	@Autowired
	GroupBuyPayRepository groupItemPayRepository;
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	GroupBuyItemRepository groupBuyItemRepository;
	@Autowired
	OptionRepository optionRepository;
	@Autowired
	WalletRepository walletRepository;
	@Autowired
	UserRepository userRepository;


	public ResponseDTO getPayListByStore(GroupItemPayDTO dto) throws Exception {
		try {
			if(authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication()) == -1){
				throw new Exception("권한 오류");
			}
			// 현재 이벤트 찾기
			List<GroupItemPays> pays = groupItemPayRepository.findByStoreId(dto.getStorePk());
			// 비어있으면 빈값 주기
			if(pays.isEmpty()) {
				return new ResponseDTO();
			}else {
				List<GroupItemPayDTO> dtos = new ArrayList<>();
				for(GroupItemPays pay : pays) {
					dtos.add(GroupItemPayDTO.toDTO(pay));
				}
				return new ResponseDTO(dtos, null);
			}
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	public void payGroupItem(GroupItemPayDTO dto) throws Exception {
		try {
			// 인증 및 지갑 가져오기
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			Optional<Wallets> walletOpt = walletRepository.findByUser_Id(user.getId());
			// 현재 이벤트 찾기
			Optional<Items> itemOpt = itemRepository.findById(dto.getItemPk());
			Optional<GroupBuyItems> groupItemOpt = groupBuyItemRepository.findById(dto.getGroupItemPk());
			// 비어있으면 빈값 주기
			if(itemOpt.isEmpty() || groupItemOpt.isEmpty() || walletOpt.isEmpty()){
				throw new Exception("정보 조회 오류 오류");
			}
			else {
				Wallets wallet = walletOpt.get();
				GroupBuyItems groupItem = groupItemOpt.get();
				Items item = itemOpt.get();
				// 공동 구매 정보와 비교
				if(!groupItem.getItem().equals(item)) {
					throw new Exception("공동 구매 물건 오류");
				}
				Options selectedOption = new Options();
				boolean isOption = false;
				for(Options option : item.getOptions()){
					if(option.getId().equals(dto.getOptionPk())){
						selectedOption = option;
						isOption = true;
					}
				}
				if(!isOption){
					throw new Exception("옵션 정보 오류");
				}else if(dto.getAmount() - groupItem.getNowAmount() > 0){
					throw new Exception("재고 소진");
				}else{
					double price = (item.getPrice() + selectedOption.getAddPrice()) * dto.getAmount();
					if(dto.getAmount() >= groupItem.getLimitPerBuyer()){
						price = price * 0.75; // 최소 수량 초과에 한하여 25% 할인
					}else{
						price = price * 0.9; // 기본 10% 할인
					}
					if(wallet.getCash() < price){
						throw new Exception("잔액 부족");
					}
					else{
						wallet.setCash(wallet.getCash() - (int)price);
						// 결제 내역 생성
						GroupItemPays pay = new GroupItemPays();
						pay.setItem(item);
						pay.setItemTitle(item.getTitle());
						pay.setAmount(dto.getAmount());
						pay.setAddress(dto.getAddress());
						pay.setOptionInfo(selectedOption.getOptionInfo());
						pay.setDeliverInfo(DeliverType.Preparing);
						pay.setGroupItemPk(groupItem.getId());
						pay.setUser(user);
						pay.setPay((int)price);
						// 결제 추가
						groupItemPayRepository.save(pay);
						walletRepository.save(wallet);
						// 재고 차감
						groupItem.setNowAmount(groupItem.getNowAmount() - dto.getAmount());
						selectedOption.setAmount(selectedOption.getAmount() - dto.getAmount());
						groupBuyItemRepository.save(groupItem);
						optionRepository.save(selectedOption);
					}
				}
			}
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO getMyGroupPayList(Integer year) throws Exception {
		try {
			Users user = authDAO.auth(SecurityContextHolder.getContext().getAuthentication());
			// 현재 이벤트 찾기
			List<GroupItemPays> pays = groupItemPayRepository.findByUserIdAndYear(user.getId(), year);
			// 비어있으면 빈값 주기
			if(pays.isEmpty()) {
				return new ResponseDTO();
			}else {
				List<GroupItemPayDTO> dtos = new ArrayList<>();
				for(GroupItemPays pay : pays) {
					dtos.add(GroupItemPayDTO.toDTO(pay));
				}
				return new ResponseDTO(dtos, null);
			}
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	public void deliveringCheck(GroupItemPayDTO dto) throws Exception {
		try {
			int sellerId = authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication());
			if(authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication()) == -1){
				throw new Exception("권한 오류");
			}
			Optional<GroupItemPays> payOpt = groupItemPayRepository.findById(dto.getId());
			if(payOpt.isEmpty()){
				throw new Exception("정보 조회 오류");
			}
			else{
				GroupItemPays pay = payOpt.get();
				// 결제된 물건이 판매자의 물건이 아닐 때
				if(!pay.getItem().getStore().getSeller().getId().equals(sellerId)){
					throw new Exception("판매자 권한 오류");
				}else{
					// 그룹 아이템 정보 가져오기 & 구매자 가져오기
					Optional<GroupBuyItems> itemOpt = groupBuyItemRepository.findById(pay.getGroupItemPk());
					Optional<Users> userOpt = userRepository.findById(pay.getUser().getId());
					if(itemOpt.isEmpty() || userOpt.isEmpty()){
						throw new Exception("등록된 물건 조회 오류");
					}
					else{
						GroupBuyItems item = itemOpt.get();
						// 이벤트 마감이 아직 안됨.
						if(item.getEvent().getEndDate().isAfter(LocalDateTime.now())){
							throw new Exception("배송 처리 기간 오류");
						}else if(!pay.getDeliverInfo().equals(DeliverType.Preparing)){
							throw new Exception("배송 처리 오류");
						}else{
							// 구매자 지갑 가져오기
							Optional<Wallets> walletOpt = walletRepository.findByUser_Id(userOpt.get().getId());
							if(walletOpt.isEmpty()){
								throw new Exception("지갑 조회 오류");
							}
							else{
								Wallets wallet = walletOpt.get();
								// 캐시백 계산
								Integer cashBack = PayCulculator.cashBackCulc(
										item.getMaxAmount(), item.getNowAmount(), pay.getPay());

								wallet.setCashBack(cashBack);
								pay.setDeliverInfo(DeliverType.Delivering);

								walletRepository.save(wallet);
								groupItemPayRepository.save(pay);
							}
						}

					}
				}
			}
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	public void deliveringCheckTotal(GroupItemPayDTO dto) throws Exception {
		try {
			int sellerId = authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication());
			if(authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication()) == -1){
				throw new Exception("권한 오류");
			}
			for(Integer id : dto.getPayIdList()){
				Optional<GroupItemPays> payOpt = groupItemPayRepository.findById(id);
				if(payOpt.isEmpty()){
					throw new Exception("정보 조회 오류");
				}
				else{
					GroupItemPays pay = payOpt.get();
					// 결제된 물건이 판매자의 물건이 아닐 때
					if(!pay.getItem().getStore().getSeller().getId().equals(sellerId)){
						throw new Exception("판매자 권한 오류");
					}else{
						// 그룹 아이템 정보 가져오기 & 구매자 가져오기
						Optional<GroupBuyItems> itemOpt = groupBuyItemRepository.findById(pay.getGroupItemPk());
						Optional<Users> userOpt = userRepository.findById(pay.getUser().getId());
						if(itemOpt.isEmpty() || userOpt.isEmpty()){
							throw new Exception("등록된 물건 조회 오류");
						}
						else{
							GroupBuyItems item = itemOpt.get();
							// 이벤트 마감이 아직 안됨.
							if(item.getEvent().getEndDate().isAfter(LocalDateTime.now())){
								throw new Exception("배송 처리 기간 오류");
							}else if(!pay.getDeliverInfo().equals(DeliverType.Preparing)){
								throw new Exception("배송 처리 오류");
							}else{
								// 구매자 지갑 가져오기
								Optional<Wallets> walletOpt = walletRepository.findByUser_Id(userOpt.get().getId());
								if(walletOpt.isEmpty()){
									throw new Exception("지갑 조회 오류");
								}
								else{
									Wallets wallet = walletOpt.get();
									// 캐시백 계산
									Integer cashBack = PayCulculator.cashBackCulc(
											item.getMaxAmount(), item.getNowAmount(), pay.getPay());

									wallet.setCashBack(cashBack);
									pay.setDeliverInfo(DeliverType.Delivering);

									walletRepository.save(wallet);
									groupItemPayRepository.save(pay);
								}
							}

						}
					}
				}
			}
		}catch(Exception e) {
			throw e;
		}
	}


	@Transactional
	public void deliveredCheck(GroupItemPayDTO dto) throws Exception {
		try {
			int sellerId = authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication());
			if(authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication()) == -1){
				throw new Exception("권한 오류");
			}
			Optional<GroupItemPays> payOpt = groupItemPayRepository.findById(dto.getId());
			if(payOpt.isEmpty()){
				throw new Exception("정보 조회 오류");
			}
			else{
				GroupItemPays pay = payOpt.get();
				// 결제된 물건이 판매자의 물건이 아닐 때
				if(!pay.getItem().getStore().getSeller().getId().equals(sellerId)) {
					throw new Exception("판매자 권한 오류");
				}else {

					if (!pay.getDeliverInfo().equals(DeliverType.Delivering)) {
						throw new Exception("배송 처리 오류");
					} else {
						pay.setDeliverInfo(DeliverType.Delivered);
						groupItemPayRepository.save(pay);
					}
				}
			}
		}catch(Exception e) {
			throw e;
		}
	}
}
