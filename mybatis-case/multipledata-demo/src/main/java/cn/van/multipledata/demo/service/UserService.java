package cn.van.multipledata.demo.service;

import cn.van.multipledata.demo.entity.UserInfo;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserService
 *
 * @author: Van
 * Date:     2019-07-30 17:43
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public interface UserService {
    /**
     * 主库（master）的全部用户数据
     *
     * @return
     */
    List<UserInfo> selectMaster();

    /**
     * 从库(slave1)全部用户数据
     *
     * @return
     */
    List<UserInfo> selectSlave();

}
