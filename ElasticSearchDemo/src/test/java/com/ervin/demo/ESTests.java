package com.ervin.demo;

import com.alibaba.fastjson2.JSON;
import com.ervin.demo.Entity.Article;
import com.ervin.demo.Entity.Author;
import com.ervin.demo.Repository.ArticleRepo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.elasticsearch.index.query.QueryBuilders;

import javax.annotation.Resource;

import java.util.Arrays;

import static java.util.Arrays.asList;

@SpringBootTest
class ESTests {
	@Resource
	ArticleRepo articleRepo;
	@Resource
	ElasticsearchRestTemplate elasticsearchRestTemplate;

	@Test
	public void testSave(){
		Article article = new Article("hello");
		article.setAuthors(asList(new Author("a1"),new Author("a2")));
		articleRepo.save(article);
	}

	@Test
	public void testQueryAuthorName(){
		Page<Article> articles = articleRepo.findByAuthorsName("a1", PageRequest.of(0,10));
		for(Article article:articles.getContent()){
			System.out.println(article);
			for(Author author:article.getAuthors()){
				System.out.println(author);
			}
		}
	}

	@Test
	public void testUpdate(){
		Page<Article> articles = articleRepo.findByTitle("a1", PageRequest.of(0,10));
		Article article = articles.getContent().get(0);
		System.out.println(article.getAuthors().get(0));
		Author author = new Author("newa1");
		article.setAuthors(Arrays.asList(author));
		articleRepo.save(article);
	}

	@Test
	public void testDelete(){
		Page<Article> articles = articleRepo.findByTitleIsContaining("a1", PageRequest.of(0,10));
		Article article = articles.getContent().get(0);
		articleRepo.delete(article);
	}

	@Test
	public void queryTileContainByTemplate(){
		Query query = new NativeSearchQueryBuilder()
				.withFilter(QueryBuilders.regexpQuery("title",".*ell.*")).build();
		SearchHits<Article> articles = elasticsearchRestTemplate.search(query,Article.class, IndexCoordinates.of("blog"));
		System.out.println(JSON.toJSONString(articles));
	}

	@Test
	void contextLoads() {
	}
}
