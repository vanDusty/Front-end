# Mybatis-Case
Mybatis 使用案例


1. [Mybatis代码生成器Mybatis-Generator使用详解](https://www.cnblogs.com/throwable/p/12046848.html)


1. Mybatis返回map集合时，列的顺序与select不一致

```xml
<select id="queryPercentByAsset" resultType="java.util.HashMap">
// 将Hashmap换成LinkedHashMap即可
<select id="queryPercentByAsset" resultType="java.util.LinkedHashMap">
```