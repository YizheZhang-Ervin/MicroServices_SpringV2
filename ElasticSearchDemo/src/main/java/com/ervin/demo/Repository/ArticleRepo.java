package com.ervin.demo.Repository;

import com.ervin.demo.Entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ArticleRepo extends ElasticsearchRepository<Article,String> {
    Page<Article> findByAuthorsName(String name, Pageable pageable);

    Page<Article> findByTitleIsContaining(String word,Pageable pageable);

    Page<Article> findByTitle(String title,Pageable pageable);
}
