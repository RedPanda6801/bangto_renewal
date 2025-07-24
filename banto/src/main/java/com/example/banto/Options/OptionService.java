package com.example.banto.Options;

import com.example.banto.Authentications.AuthService;
import com.example.banto.Items.Items;
import com.example.banto.Items.ItemRepository;
import com.example.banto.Repositorys.OptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;
    private final ItemRepository itemRepository;
    private final AuthService authService;

    @Transactional
    public void addItemOption(OptionDTO dto) throws Exception{
        try {
            int sellerId = authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication());
            if(sellerId == -1 && !authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
                throw new Exception("판매자 권한 오류");
            }
            Optional<Items> itemOpt = itemRepository.findById(dto.getItemPk());
            if(itemOpt.isEmpty()) {
                throw new Exception("물건 조회 오류");
            }else if(!itemOpt.get().getStore().getSeller().getUser().getId().equals(sellerId)){
                throw new Exception("판매자 정보 불일치");
            }else {
                Items item = itemOpt.get();
                Options option = new Options();
                option.setOptionInfo(dto.getOptionInfo());
                option.setAddPrice(dto.getAddPrice());
                option.setAmount(dto.getAmount());
                option.setItem(item);
                optionRepository.save(option);
            }
        }catch(Exception e) {
            throw e;
        }
    }

    @Transactional
    public void modifyItemOption(OptionDTO dto) throws Exception{
        try {
            int sellerId = authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication());
            if(sellerId == -1 && !authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
                throw new Exception("판매자 권한 오류");
            }
            Optional<Options> optionOpt = optionRepository.findById(dto.getId());
            if(optionOpt.isEmpty()) {
                throw new Exception("매장 조회 오류");
            }else if(!optionOpt.get().getItem().getStore().getSeller().getUser().getId().equals(sellerId)){
                throw new Exception("판매자 정보 불일치");
            }else {
                Options option = optionOpt.get();
                // 수정 로직
                option.setAddPrice((dto.getAddPrice() != null && !dto.getAddPrice().equals("")) ?
                    dto.getAddPrice() : option.getAddPrice());
                option.setOptionInfo((dto.getOptionInfo() != null && !dto.getOptionInfo().equals("")) ?
                    dto.getOptionInfo() : option.getOptionInfo());
                option.setAmount((dto.getAmount() != null && !dto.getAmount().equals("")) ?
                    dto.getAmount() : option.getAmount());
                optionRepository.save(option);
            }
        }catch(Exception e) {
            throw e;
        }
    }

    @Transactional
    public void deleteOption(OptionDTO dto) throws Exception {
        try {
            int sellerId = authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication());
            if (sellerId == -1 && !authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())) {
                throw new Exception("판매자 권한 오류");
            }

            Optional<Options> optionOpt = optionRepository.findById(dto.getId());
            if (optionOpt.isEmpty()) {
                throw new Exception("매장 조회 오류");
            } else if (!optionOpt.get().getItem().getStore().getSeller().getUser().getId().equals(sellerId)) {
                throw new Exception("판매자 정보 불일치");
            } else if (optionOpt.get().getItem().getOptions().size() == 1) {
                throw new Exception("필수 옵션 제거 불가");
            } else {
                Options option = optionOpt.get();
                Items item = option.getItem();

                // 연관 엔티티 정리
                option.getCarts().clear();
                option.getEventItems().clear();

                item.getOptions().remove(option);

                // 삭제 실행
                optionRepository.delete(option);
                optionRepository.flush();
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
