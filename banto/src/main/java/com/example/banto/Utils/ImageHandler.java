package com.example.banto.Utils;

import com.example.banto.Exceptions.CustomExceptions.ImageHandleException;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class ImageHandler {
    public static String imageMapper(MultipartFile file, String path){
        try{
            // 1. 파일 이름 가져오기
            String originFileName = file.getOriginalFilename();
            // 2. dot을 기준으로 확장자 분리
            int dotInd = originFileName.indexOf(".");
            // 2-1. 파일 이름
            String before = originFileName.substring(0, dotInd);
            // 2-2. 확장자
            String ext = originFileName.substring(dotInd);
            // 3. 파일 이름에 UUID(식별자) 추가
            String newFileName = before + "(" + UUID.randomUUID() + ")" + ext;
            // 4. 정해둔 경로에 파일 저장
            file.transferTo(new java.io.File(path + newFileName));
            // 5. 생성된 파일 이름 반환
            return originFileName;
        }catch (Exception e){
            // 5-7. 파일 저장 실패 시 예외 처리
            throw new ImageHandleException("이미지 저장에 오류가 발생했습니다.");
        }
    }
}
