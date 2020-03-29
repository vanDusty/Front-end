package cn.van.mybatis.demo.mapper;

import cn.van.mybatis.demo.entity.UserInfoLikeDO;

import java.util.List;

/**
 * Copyright (C), 2015-2020, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserInfoMapper
 *
 * @author: Van
 * Date:     2020-02-02 15:12
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public interface UserInfoLikeMapper {

    List<UserInfoLikeDO> selectByKeyWord(String nickName);

    List<UserInfoLikeDO> selectByWord(String nickName);

    List<UserInfoLikeDO> selectForConcat(String nickName);

    List<UserInfoLikeDO> selectForBind(String nickName);

}
