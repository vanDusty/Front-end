# Mybatis-Case
Mybatis 使用案例


1. Mybatis返回map集合时，列的顺序与select不一致

```xml
<select id="queryPercentByAsset" resultType="java.util.HashMap">
// 将Hashmap换成LinkedHashMap即可
<select id="queryPercentByAsset" resultType="java.util.LinkedHashMap">
```

1. choose (when,otherwize) ,相当于java 语言中的 switch

```xml

<select id="dynamicChooseTest" parameterType="Blog" resultType="Blog">
    select * from table_name where 1 = 1 
    <choose>
        <when test="title != null">
            and title = #{title}
        </when>
        <when test="content != null">
            and content = #{content}
        </when>
        <otherwise>
            and owner = "owner1"
        </otherwise>
    </choose>
</select>
```

when元素表示当when中的条件满足的时候就输出其中的内容，跟JAVA中的switch效果差不多的是按照条件的顺序，当when中有条件满足的时候，就会跳出choose，即所有的when和otherwise条件中，只有一个会输出，当所有的我很条件都不满足的时候就输出otherwise中的内容。所以上述语句的意思非常简单， 当title!=null的时候就输出and titlte = #{title}，不再往下判断条件，当title为空且content!=null的时候就输出and content = #{content}，当所有条件都不满足的时候就输出otherwise中的内容。

1. trim (对包含的内容加上 prefix,或者 suffix 等，前缀，后缀) 
   
```xml
<select id="dynamicTrimTest" parameterType="Blog" resultType="Blog">
    select * from table_name 
    <trim prefix="where" prefixOverrides="and |or">
        <if test="title != null">
            title = #{title}
        </if>
        <if test="content != null">
            and content = #{content}
        </if>
        <if test="owner != null">
            or owner = #{owner}
        </if>
    </trim>
</select>
```

前缀/后缀，与之对应的属性是prefix和suffix；可以把包含内容的首部某些内容覆盖，即忽略，也可以把尾部的某些内容覆盖，对应的属性是prefixOverrides和suffixOverrides；正因为trim有这样的功能，所以我们也可以非常简单的利用trim来代替where元素的功能。

1. foreach元素的属性主要有item，index，collection，open，separator，close。
    1. item：集合中元素迭代时的别名，
    1. index：集合中元素迭代时的索引
    1. open：常用语where语句中，表示以什么开始，比如以'('开始
    1. separator：表示在每次进行迭代时的分隔符，
    1. close 常用语where语句中，表示以什么结束，
    
在使用foreach的时候最关键的也是最容易出错的就是collection属性，该属性是必须指定的，但是在不同情况下，该属性的值是不一样的，主要有一下3种情况：
    1. 如果传入的是单参数且参数类型是一个List的时候，collection属性值为list .
    1. 如果传入的是单参数且参数类型是一个array数组的时候，collection的属性值为array .
    1. 如果传入的参数是多个的时候，我们就需要把它们封装成一个Map了，当然单参数也可以封装成map，实际上如果你在传入参数的时候，在MyBatis里面也是会把它封装成一个Map的，map的key就是参数名，所以这个时候collection属性值就是传入的List或array对象在自己封装的map里面的key.

```xml
<select id="queryById" resultMap="BaseResultMap" >
  select * FROM entity
  where id in 
  <foreach collection="userids" item="userid" index="index" open="(" separator="," close=")">
          #{userid}
  </foreach>
</select>
```

1. 如何执行批量插入?

```java
List<string> names = new Arraylist();
names.add(“fred”);
names.add(“barney”);
names.add(“betty”);
names.add(“wilma”);

// 注意这里 executortype.batch
Sqlsession sqlsession = Sqlsessionfactory.opensession(Executortype.batch);
try {
 Namemapper mapper = sqlsession.getmapper(namemapper.class);
 for (string name : names) {
     // 单个插入sql
     mapper.insertname(name);
 }
 sqlsession.commit();
}catch(Exception e){
 e.printStackTrace();
 sqlSession.rollback(); 
 throw e; 
}
 finally {
     sqlsession.close();
}
```