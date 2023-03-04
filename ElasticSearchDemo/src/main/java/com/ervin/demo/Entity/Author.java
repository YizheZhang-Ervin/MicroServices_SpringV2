package com.ervin.demo.Entity;

import lombok.Data;

@Data
public class Author {
    String name;
    public Author(String name){
        this.name = name;
    }
}
