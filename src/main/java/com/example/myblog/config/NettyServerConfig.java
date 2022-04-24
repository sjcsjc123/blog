package com.example.myblog.config;

import com.example.myblog.config.handler.NettyChildHandler;
import com.example.myblog.config.server.NettyWebSocketServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * netty服务端配置
 * @author SJC
 */
@Configuration
public class NettyServerConfig {

    @Autowired
    private NettyChildHandler nettyChildHandler;

    @Bean
    public NettyWebSocketServer nettyWebSocketServer(){
        NettyWebSocketServer nettyWebSocketServer = new NettyWebSocketServer();
        nettyWebSocketServer.setPort(9999);
        nettyWebSocketServer.setChildChannelHandler(nettyChildHandler);
        return nettyWebSocketServer;
    }

    @Bean("bossGroup")
    public EventLoopGroup getBossGroup() {
        return new NioEventLoopGroup();
    }

    @Bean("workerGroup")
    public EventLoopGroup getWorkerGroup() {
        return new NioEventLoopGroup();
    }

    @Bean
    public ServerBootstrap getServerBootstrap(){
        return new ServerBootstrap();
    }
}
