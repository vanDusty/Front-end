/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: UserApplication
 * Author:   zhangfan
 * Date:     2019-01-29 17:09
 * Description: 用户模块启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.mybatis.druid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.van.mybatis.druid.mapper")
public class DruidApplication {

    public static void main(String[] args) {
        SpringApplication.run(DruidApplication.class, args);
    }
}