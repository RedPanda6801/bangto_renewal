package com.example.banto.Items;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.example.banto.Enums.CategoryType;
import com.example.banto.Exceptions.CustomExceptions.InternalServerException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomItemElasticsearchRepositoryImpl implements CustomItemElasticsearchRepository{
    private final ElasticsearchClient client;

    @Override
    public SearchResponse<ItemDocument> searchItemListWithFilter(SearchDTO dto) {
        // 1. 동적 쿼리 인스턴스 생성
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        // 2. 필드 가져오기
        String title = dto.getTitle() != null ? dto.getTitle() : null;
        String storeName = dto.getStoreName() != null ? dto.getStoreName() : null;
        CategoryType category = dto.getCategory() != null ? dto.getCategory() : null;
        Integer minPrice = dto.getMinPrice() != null ? dto.getMinPrice() : null;
        Integer maxPrice = dto.getMaxPrice() != null ? dto.getMaxPrice() : null;
        int page = dto.getPage() != null ? dto.getPage() : 0;
        // 3. 필터링
        if (title != null && !title.trim().isEmpty()) {
            boolQueryBuilder.must(MatchQuery.of(m -> m.field("title").query(title))._toQuery());
        }
        if (category != null) {
            boolQueryBuilder.filter(TermQuery.of(t -> t.field("category").value(category.name()))._toQuery());
        }
        if (storeName != null) {
            boolQueryBuilder.filter(MatchQuery.of(t -> t.field("storeName").query(storeName))._toQuery());
        }
        if (minPrice != null || maxPrice != null) {
            RangeQuery.Builder rangeQueryBuilder = new RangeQuery.Builder().field("price");
            if (minPrice != null) rangeQueryBuilder.gte(JsonData.of(minPrice));
            if (maxPrice != null) rangeQueryBuilder.lte(JsonData.of(maxPrice));
            boolQueryBuilder.filter(rangeQueryBuilder.build()._toQuery());
        }
        try{
            BoolQuery boolQuery = boolQueryBuilder.build();
            // 4. 크기 조절 및 elasticsearch 조회
            return client.search(s -> s
                .index("items")
                .from(page * 20)     // 시작 지점
                .size(20)    // 최대 20개만 조회
                .query(q -> q.bool(boolQuery)), ItemDocument.class);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Exception message: " + e.getMessage());
            throw new InternalServerException("물건 필터링 조회에 오류가 생겼습니다.");
        }
    }
}
