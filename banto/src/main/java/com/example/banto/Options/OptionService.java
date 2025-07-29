package com.example.banto.Options;

import com.example.banto.Authentications.AuthService;
import com.example.banto.Boards.Comments.Comments;
import com.example.banto.Configs.EnvConfig;
import com.example.banto.Exceptions.DeletionConstraintException;
import com.example.banto.Exceptions.ForbiddenException;
import com.example.banto.Exceptions.ResourceNotFoundException;
import com.example.banto.Items.Items;
import com.example.banto.Items.ItemRepository;
import com.example.banto.Boards.Qnas.QNAs;
import com.example.banto.SoldItems.SoldItems;
import com.example.banto.Utils.ImageHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;
    private final ItemRepository itemRepository;
    private final OptionImagesRepository optionImagesRepository;
    private final AuthService authService;
    private final EnvConfig envConfig;

    @Transactional
    public void create(OptionDTO dto, List<MultipartFile> files) {
        // 1. 판매자 권한 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authService.authToSeller(authentication);
        // 2. 추가하려는 옵션의 물건 가져오기
        Items item = itemRepository.findById(dto.getItemPk())
            .orElseThrow(() -> new ResourceNotFoundException("물건 정보가 없습니다."));
        // 3. 물건 판매자와 옵션 추가자의 일치 확인
        if(!Objects.equals(Long.parseLong(authentication.getName()), item.getStore().getSeller().getId())){
            throw new ForbiddenException("물건에 대한 권한이 없습니다.");
        }
        // 4. 추가 로직 구현
        Options option = Options.toEntity(dto, item);
        // 5. DB 추가
        optionRepository.save(option);
        // 6. 사진 추가
        for (MultipartFile file : files){
            // 6-1. 이미지 파일 저장
            String newFileName = ImageHandler.imageMapper(file, envConfig.get("FRONTEND_UPLOAD_ADDRESS"));
            // 6-2. DB에 저장
            OptionImages image = OptionImages.toEntity(option, newFileName);
            optionImagesRepository.save(image);
        }
    }

    @Transactional
    public void update(OptionDTO dto, List<MultipartFile> files) {
        // 1. 판매자 권한 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authService.authToSeller(authentication);
        // 2. option의 id로 option 조회
        Options option = optionRepository.findById(dto.getId())
            .orElseThrow(() -> new ResourceNotFoundException("수정할 옵션이 없습니다."));
        // 3. 판매자의 option 수정 권한 확인
        if(!Objects.equals(Long.parseLong(authentication.getName()), option.getItem().getStore().getSeller().getId())){
            throw new ForbiddenException("옵션에 대한 권한이 없습니다.");
        }
        // 4. 수정 로직
        option.setAddPrice((dto.getAddPrice() != null) ?
            dto.getAddPrice() : option.getAddPrice());
        option.setOptionInfo((dto.getOptionInfo() != null && !dto.getOptionInfo().trim().isEmpty()) ?
            dto.getOptionInfo() : option.getOptionInfo());
        option.setAmount((dto.getAmount() != null) ?
            dto.getAmount() : option.getAmount());
        // 5. 현재 파일 가져오기
        List<OptionImages> currentImages = option.getOptionImages();
        List<String> currentImageNames = currentImages.stream().map(img ->{
            try{
                return img.getImgUrl();
            }catch(Exception e){
                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
        // 6. 추가 및 삭제한 파일 목록 불러오기
        if(!files.isEmpty()){
            for(MultipartFile file : files){
                // 6-1. 중복 제거 후 넘기기
                if(currentImageNames.contains(file.getOriginalFilename())){
                    currentImageNames.remove(file.getOriginalFilename());
                    continue;
                }
                // 6-2. 파일 추가 로직
                String newFileName = ImageHandler.imageMapper(file, envConfig.get("FRONTEND_UPLOAD_ADDRESS"));
                OptionImages image = OptionImages.toEntity(option, newFileName);
                optionImagesRepository.save(image);
            }
            // 7. 제거된 이미지에 대한 삭제 처리
            for(OptionImages beforeImage : currentImages){
                // 6-1. 중복된 파일 여부 확인
                if(currentImageNames.contains(beforeImage.getImgUrl())){
                    beforeImage.setOption(null);
                    optionImagesRepository.delete(beforeImage);
                }
            }
        }
    }

    @Transactional
    public void delete(OptionDTO dto) {
        // 1. 판매자 권한 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authService.authToSeller(authentication);
        // 2. option의 id로 option 조회
        Options option = optionRepository.findById(dto.getId())
            .orElseThrow(() -> new ResourceNotFoundException("삭제할 옵션이 없습니다."));
        // 3. 판매자의 option 수정 권한 확인
        if (!Objects.equals(Long.parseLong(authentication.getName()), option.getItem().getStore().getSeller().getId())) {
            throw new ForbiddenException("옵션에 대한 권한이 없습니다.");
        }
        // 4. option은 한개 이상이여야 함
        if (option.getItem().getOptions().size() <= 1) {
            throw new DeletionConstraintException("옵션은 한 개 이상이여야 합니다.");
        }
        // 5. comment 전부 setNull
        for(Comments comment : option.getComments()){
            comment.setOption(null);
        }
        // 6. qna 전부 setNull
        for(QNAs qna : option.getQnas()){
            qna.setOption(null);
        }
        // 7. 결제된 물건 전부 setNull
        for(SoldItems soldItem : option.getSoldItems()){
            soldItem.setOption(null);
        }
        // 8. Option 이미지 삭제
        for(OptionImages optionImage : option.getOptionImages()){
            File file = new File(envConfig.get("FRONTEND_UPLOAD_ADDRESS")+optionImage.getImgUrl());
            if (file.exists()) {
                if(file.delete()){
                    continue;
                }else{
                    throw new DeletionConstraintException("파일 삭제에 실패했습니다.");
                }
            }
        }
        // 9. DB에 반영
        optionRepository.delete(option);
    }
}
