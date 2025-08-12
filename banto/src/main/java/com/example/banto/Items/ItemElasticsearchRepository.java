package com.example.banto.Items;

import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemElasticsearchRepository extends ElasticsearchRepository<ItemDocument, Long>, CustomItemElasticsearchRepository {
    SearchHits<ItemDocument> findByTitle(String title);
}
