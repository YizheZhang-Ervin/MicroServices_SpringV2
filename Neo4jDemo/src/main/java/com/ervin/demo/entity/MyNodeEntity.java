package com.ervin.demo.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.io.Serializable;

@Data
@Node("myNode")
public class MyNodeEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String name;
    @Property
    private String age;
    @Property
    private String sex;
}
