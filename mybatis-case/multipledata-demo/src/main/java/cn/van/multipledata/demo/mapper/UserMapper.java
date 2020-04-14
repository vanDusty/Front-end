package cn.van.multipledata.demo.mapper;

import cn.van.multipledata.demo.entity.UserInfo;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserMapper
 *
 * @author: Van
 * Date:     2019-07-30 15:48
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public interface UserMapper {

    /**
     * 查询所有用户信息
     * @return
     */
    List<UserInfo> selectAll();
}

