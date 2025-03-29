package com.ervin.demo.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField(value = "username")
    private String name;
    private Integer age;
    private String email;
    @TableField(fill = FieldFill.INSERT)
    private Data createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Data updateTime;
    @Version    //乐观锁version注解
    private Integer version;
    @TableLogic //逻辑删除
    private Integer deleted;
}
