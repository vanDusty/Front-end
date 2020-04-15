package cn.van.multipledata.demo.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 继承 AbstractRoutingDataSource 重写 determineCurrentLookupKey()，动态实现根据请求不同达到切换数据源的需求。
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 获取路由
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        // 根据MyThreadLocal.getLocal()判断该次请求使用的是哪个数据源
        return DataSourceContextHolder.get();
    }

}
