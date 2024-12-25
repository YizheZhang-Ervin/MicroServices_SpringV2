package com.ervin.demo.dao;

import com.ervin.demo.entity.MyNodeEntity;
import com.ervin.demo.entity.MyNodeRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MyNodeDao extends Neo4jRepository<MyNodeEntity, Long> {

    //一对一手动指定关系
    @Query("match (n:myNode {name:{0}}),(m:myNode {name:{2}})" +
            "create (n)-[:人物关系{relation:{1}}]->(m)")
    void createRelation(String from, String relation, String to);

    //根据关系数据进行当前用户的所有关系生成
    @Query("match (n:myNode {name:{0}}),(m:dmRelation),(s:myNode) where m.from={0} and s.name=m.to create(n)-[:人物关系 {relation:m.relation}]->(s)")
    void createRelationByName(String fromName);

    //根据关系数据进行当前用户的所有关系生成
    @Query("CALL db.relationshipTypes()")
    List<String> getAllRealationTypes();

    //修改
    @Query("MATCH (n) WHERE id(n) = :#{#myNodeEntity.id} SET n.name = :#{#myNodeEntity.name},n.age = :#{#myNodeEntity.age},n.sex = :#{#myNodeEntity.sex} RETURN n")
    MyNodeEntity updateById(@Param("myNodeEntity") MyNodeEntity myNodeEntity);

    @Query("match (n:myNode {name:{name}})-[r:`人物关系`]->(m:myNode) where r.relation={relation} return m")
    List<MyNodeEntity> getRelationsByName(@Param("name") String name, @Param("relation") String relation);

    @Query("MATCH (n:myNode {name:'xxx'}) RETURN n")
    MyNodeEntity getTest();

    @Query(value="match (n:myNode {name:{name}})-[r:`人物关系`]->(m:myNode) return id(n) as pid, n.name as name,r.relation as relation" +
            ",m as myNode skip {skip} limit {pageSize}"
            ,countQuery = "match (n:myNode {name:{name}})-[r:`人物关系`]->(m:myNode) return count(r)")
    Page<MyNodeRelation> getRelationsByName(@Param("name")String name, @Param("skip")int ship, @Param("pageSize")int pageSize, Pageable pageable);

}

