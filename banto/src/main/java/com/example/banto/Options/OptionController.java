package com.example.banto.Options;

import com.example.banto.Items.ItemDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class OptionController {

    private final OptionService optionService;

    // 옵션 추가
    @PostMapping(path = "/seller/option/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createOption(@Valid @RequestPart("dto") OptionDTO optionDTO
        , @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        optionService.create(optionDTO, files);
        return ResponseEntity.ok().body("옵션 삭제에 성공했습니다.");
    }

    // 옵션 수정
    @PostMapping(path = "/seller/option/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@Valid @RequestPart("dto") OptionDTO optionDTO
        , @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        optionService.update(optionDTO ,files);
        return ResponseEntity.ok().body("옵션 수정에 성공했습니다.");
    }
    // 옵션 삭제
    @PostMapping("/seller/item/option/delete")
    public ResponseEntity<?> delete(@RequestBody OptionDTO optionDTO) {
        optionService.delete(optionDTO);
        return ResponseEntity.ok().body("옵션이 삭제되었습니다.");
    }
}
