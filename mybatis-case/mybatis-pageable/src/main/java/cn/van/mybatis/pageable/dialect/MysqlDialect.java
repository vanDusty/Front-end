package cn.van.mybatis.pageable.dialect;

import org.springframework.stereotype.Component;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: MysqlDialect
 *
 * @author: Van
 * Date:     2019-06-28 19:06
 * Description: Mysql 分页方言
 * Version： V1.0
 */
@Component
public class MysqlDialect implements Dialect{

    private static final String PATTERN = "%s limit %s, %s";

    private static final String PATTERN_FIRST = "%s limit %s";

    @Override
    public String getLimitSql(String targetSql, int offset, int limit) {
        if (offset == 0) {
            return String.format(PATTERN_FIRST, targetSql, limit);
        }

        return String.format(PATTERN, targetSql, offset, limit);
    }
}
