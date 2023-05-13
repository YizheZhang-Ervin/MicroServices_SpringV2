package com.ervin.demo.microwebserver.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.ervin.demo.microwebserver.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(chain = true)
@TableName("jwt")
@AllArgsConstructor
@NoArgsConstructor
public class Jwt {
    @TableId(value="id",type= IdType.AUTO)
    private Long id;  //对应表中BigInt类型

    private String userId;

    private String encryptedPwd;

    private String token;

    private String refreshToken;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Date tokenExpireTime;

    private Date refreshTokenExpireTime;

    @TableLogic
    private Integer isDeleted;
}