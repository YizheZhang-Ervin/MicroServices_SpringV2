package com.ervin.demo.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "blog")
@Data
public class Article {
    @Id
    String id;
    String title;
    @Field(type= FieldType.Nested,includeInParent=true)
    List<Author> authors;
    public Article(String title){
        this.title = title;
    }
}
