package cn.van.mybatis.demo.like;

import cn.van.mybatis.demo.BaseTest;
import cn.van.mybatis.demo.entity.UserInfoDO;
import cn.van.mybatis.demo.mapper.UserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (C), 2015-2020, 风尘博客
 * 公众号 : 风尘博客
 * FileName: MybatisLikeTest
 *
 * @author: Van
 * Date:     2020-02-02 15:49
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Slf4j
public class MybatisLikeTest extends BaseTest {

    @Resource
    UserInfoMapper userInfoMapper;

    /**
     * 直接传参法
     */
    @Test
    public void selectTest() {
        String nickName = "%" + "王" + "%";
        List<UserInfoDO> list = userInfoMapper.selectByKeyWord(nickName);
        log.info("UserList:{}", list);
    }
    /**
     * 使用 ${...} 代替 #{...}
     */
    @Test
    public void selectByWordTest() {
        String nickName ="王";
        List<UserInfoDO> list = userInfoMapper.selectByWord(nickName);
        log.info("UserList:{}", list);
    }

    /**
     * CONCAT（）函数
     */
    @Test
    public void concatTest() {
        String nickName = "王";
        List<UserInfoDO> list = userInfoMapper.selectForConcat(nickName);
        log.info("UserList:{}", list);
    }

    /**
     * Mybatis的bind
     */
    @Test
    public void bindTest() {
        String nickName = "王";
        List<UserInfoDO> list = userInfoMapper.selectForBind(nickName);
        log.info("UserList:{}", list);
    }
}
