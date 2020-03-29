package cn.van.mybatis.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Copyright (C), 2015-2020, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserInfoDO
 *
 * @author: Van
 * Date:     2020-02-02 15:10
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Data
public class UserInfoLikeDO {

    private Long id;

    private String userName;

    private String passWord;

    private String nickName;

    private String mobile;

    private String email;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtUpdate;

}
