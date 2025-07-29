package com.example.banto.Boards;

import com.example.banto.Utils.PageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Comment & QNA의 DIP를 위한 인터페이스
public interface BoardService<T, ID> {
    // 생성
    void create(T dto, List<MultipartFile> files);
    // 20개 조회(본인)
    PageDTO getList(int page);
    // 단일 세부 조회(판매자 또는 본인)
    T getDetail(ID id);
    // 매장 별 조회(판매자)
    PageDTO getListByStore(ID storeId, int page);
    // 물품 별 조회(세부 조회 시)
    PageDTO getListByItem(ID itemId, int page);
    // 삭제
    void delete(ID id);
}
