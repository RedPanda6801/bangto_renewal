package com.example.banto.Options;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("api")
public class OptionController {

    private final OptionService optionService;

    // 옵션 추가
    @PostMapping("/item/option/add")
    public ResponseEntity<?> AddItemOption(@RequestBody OptionDTO optionDTO) throws Exception {
        try {
            System.out.println(optionDTO);
            optionService.addItemOption(optionDTO);
            return ResponseEntity.ok().body(null);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 옵션 수정
    @PostMapping("/item/option/modify")
    public ResponseEntity modifyItemOption(@RequestBody OptionDTO optionDTO) throws Exception {
        try {
            optionService.modifyItemOption(optionDTO);
            return ResponseEntity.ok().body(null);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // 옵션 삭제
    @PostMapping("/item/option/delete")
    public ResponseEntity deleteOption(@RequestBody OptionDTO optionDTO) throws Exception {
        try {
            optionService.deleteOption(optionDTO);
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
