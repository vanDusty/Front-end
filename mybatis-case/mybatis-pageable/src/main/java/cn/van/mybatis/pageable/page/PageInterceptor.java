package cn.van.mybatis.pageable.page;

import cn.van.mybatis.pageable.dialect.Dialect;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: PageInterceptor
 *
 * @author: Van
 * Date:     2019-12-28 19:03
 * Description: 分页拦截器
 * Version： V1.0
 */
@Intercepts({
        @Signature(
                type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        )
})
@Component
@Slf4j
public class PageInterceptor implements Interceptor {


    static int MAPPED_STATEMENT_INDEX = 0;
    static int PARAMETER_INDEX = 1;

    private final Dialect dialect;

    public static final ThreadLocal<PageInterceptor.PageParam> PARAM_THREAD_LOCAL = new ThreadLocal<>();

    public PageInterceptor(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();

        PageInterceptor.PageParam pageParam = PARAM_THREAD_LOCAL.get();
        //判断是否需要分页
        if (pageParam != null) {
            final MappedStatement ms = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
            Object param = args[PARAMETER_INDEX];
            BoundSql boundSql = ms.getBoundSql(param);
            // 获取总数
            pageParam.totalSize = (int) queryTotal(ms, boundSql);
            // 计算总页数
            pageParam.totalPage = countPage(pageParam.totalSize,pageParam.limit);
            // 重新设置SQL语句映射
            args[MAPPED_STATEMENT_INDEX] = copyPageableMappedStatement(ms, boundSql);
        }
        //执行拦截对象真正的方法
        Object proceed = invocation.proceed();
        return proceed;
    }

    /**
     * 查询总记录数
     *
     * @param mappedStatement
     * @param boundSql
     * @return
     * @throws SQLException
     */
    private long queryTotal(MappedStatement mappedStatement, BoundSql boundSql) throws SQLException {

        Connection connection = null;
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {

            connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();

            String countSql = this.dialect.getCountSql(boundSql.getSql());

            countStmt = connection.prepareStatement(countSql);
            BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql,
                    boundSql.getParameterMappings(), boundSql.getParameterObject());

            setParameters(countStmt, mappedStatement, countBoundSql, boundSql.getParameterObject());

            rs = countStmt.executeQuery();
            long totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getLong(1);
            }

            return totalCount;
        } catch (SQLException e) {
            log.error("查询总记录数出错", e);
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    log.error("exception happens when doing: ResultSet.close()", e);
                }
            }

            if (countStmt != null) {
                try {
                    countStmt.close();
                } catch (SQLException e) {
                    log.error("exception happens when doing: PreparedStatement.close()", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("exception happens when doing: Connection.close()", e);
                }
            }
        }
    }


    /**
     * 对分页SQL参数(?)设值
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
                               Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps);
    }

    private MappedStatement copyPageableMappedStatement(MappedStatement ms, BoundSql boundSql) {
        PageInterceptor.PageParam pageParm = PARAM_THREAD_LOCAL.get();
        String pageSql = dialect.getLimitSql(boundSql.getSql(), pageParm.offset, pageParm.limit);
        SqlSource source = new StaticSqlSource(ms.getConfiguration(), pageSql, boundSql.getParameterMappings());
        return copyFromMappedStatement(ms, source);
    }

    /**
     * 利用方言接口替换原始的SQL语句
     *
     * @param ms
     * @param newSqlSource
     * @return
     */
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuffer keyProperties = new StringBuffer();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        //setStatementTimeout()
        builder.timeout(ms.getTimeout());

        //setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());

        //setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        //setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    //包装目标对象 为目标对象创建动态代理 按照从前到后的顺序执行
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    //获取插件初始化参数
    @Override
    public void setProperties(Properties properties) {
    }

    public static class PageParam {
        // 当前页
        int pageNum;

        // 分页开始位置
        int offset;

        // 分页数量
        int limit;

        // 总数
        public int totalSize;

        // 总页数
        public int totalPage;
    }

    /**
     * 开始分页
     *
     * @param pageNum  当前页码(从1开始)
     * @param pageSize 每页长度
     */
    public static void startPage(int pageNum, int pageSize) {
        int offset = (pageNum-1) * pageSize;
        int limit = pageSize;
        PageInterceptor.PageParam pageParam = new PageInterceptor.PageParam();
        pageParam.offset = offset;
        pageParam.limit = limit;
        pageParam.pageNum = pageNum;
        PARAM_THREAD_LOCAL.set(pageParam);
    }

    /**
     * 计算总页数
     * @param totalSize
     * @param offset
     */
    public int countPage(int totalSize, int offset) {
        int totalPageTemp = totalSize / offset;
        int plus = (totalSize % offset) == 0 ? 0 : 1;
        totalPageTemp = totalPageTemp + plus;
        if (totalPageTemp <= 0) {
            totalPageTemp = 1;
        }
        return totalPageTemp;
    }
}

