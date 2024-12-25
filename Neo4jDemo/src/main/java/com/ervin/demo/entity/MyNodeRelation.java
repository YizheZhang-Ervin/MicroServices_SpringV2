package com.ervin.demo.entity;


import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.io.Serializable;

// 关系实体
@Data
@Node("myNode")
public class MyNodeRelation implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String name;
    @Property
    private String relation;
}