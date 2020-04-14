package cn.van.multipledata.demo.config;

import cn.van.multipledata.demo.enums.DynamicDSEnum;

/**
 * @公众号： 风尘博客
 * @Classname DataSourceContexHolder
 * @Description TODO
 * @Date 2020/4/14 3:55 下午
 * @Author by Van
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<DynamicDSEnum> DYNAMIC_DATASOURCE_CONTEXT = new ThreadLocal();

    public static void set(DynamicDSEnum datasourceType) {
        DYNAMIC_DATASOURCE_CONTEXT.set(datasourceType);
    }

    public static DynamicDSEnum get() {
        return DYNAMIC_DATASOURCE_CONTEXT.get();
    }

    public static void clear() {
        DYNAMIC_DATASOURCE_CONTEXT.remove();
    }
}
