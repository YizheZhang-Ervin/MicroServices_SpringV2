package com.ervin.demo.microwebserver.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.ervin.demo.microwebserver.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("user")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @TableId(value="id",type= IdType.AUTO)
    private Long id;  //对应表中BigInt类型
    @TableField("name")
    private String name;
    private Integer age;
    private String email;
    private SexEnum sex;
    @TableLogic
    private Integer isDeleted;
    @Version
    private Integer version; // 乐观锁
}
