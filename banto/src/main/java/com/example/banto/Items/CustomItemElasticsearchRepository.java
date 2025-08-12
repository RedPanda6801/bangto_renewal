package com.example.banto.Items;

import co.elastic.clients.elasticsearch.core.SearchResponse;

public interface CustomItemElasticsearchRepository {
        SearchResponse<ItemDocument> searchItemListWithFilter(SearchDTO dto);
}
