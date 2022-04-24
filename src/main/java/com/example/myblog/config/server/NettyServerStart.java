package com.example.myblog.config.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author SJC
 */
@Component
public class NettyServerStart {

    private final Logger logger =
            LoggerFactory.getLogger(NettyServerStart.class);

    @Autowired
    private NettyWebSocketServer nettyWebSocketServer;

    private Thread nettyThread;

    @PostConstruct
    public void init(){
        nettyThread = new Thread(nettyWebSocketServer);
        logger.info("开启独立线程，启动Netty WebSocket服务器...");
        nettyThread.start();
    }

    @PreDestroy
    public void close(){
        logger.info("正在释放Netty Websocket相关连接...");
        nettyWebSocketServer.close();
        logger.info("正在关闭Netty Websocket服务器线程...");
        nettyThread.stop();
        logger.info("系统成功关闭！");
    }
}
