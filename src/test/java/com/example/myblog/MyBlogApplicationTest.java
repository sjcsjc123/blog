package com.example.myblog;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
public class MyBlogApplicationTest {
    //DI注入数据源
    @Autowired
    DataSource dataSource;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void contextLoads() throws SQLException {
        //看一下默认数据源
        System.out.println(dataSource.getClass());
        //获得连接
        Connection connection =   dataSource.getConnection();
        System.out.println(connection);

        DruidDataSource druidDataSource = (DruidDataSource) dataSource;
        System.out.println("druidDataSource 数据源最大连接数：" + druidDataSource.getMaxActive());
        System.out.println("druidDataSource 数据源初始化连接数：" + druidDataSource.getInitialSize());

        //关闭连接
        connection.close();
    }

    @Test
    void test1(){
        redisTemplate.opsForValue().set("username:zhangsan", String.valueOf(1));
        redisTemplate.opsForValue().set("username:zhangsssan",
                String.valueOf(1));
        System.out.println(redisTemplate.opsForValue().get("username" +
                ":zhangsan"));
        System.out.println(redisTemplate.opsForValue().get("refreshToken:邵佳成"));
    }
}
