package com.example.myblog.config.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author SJC
 */
public class NettyWebSocketServer implements Runnable {

    private final Logger logger =
            LoggerFactory.getLogger(NettyWebSocketServer.class);

    @Autowired
    @Qualifier("bossGroup")
    private EventLoopGroup bossGroup;

    @Autowired
    @Qualifier("workerGroup")
    private EventLoopGroup workerGroup;

    @Autowired
    private ServerBootstrap bootstrap;

    private int port;
    private ChannelHandler channelHandler;
    private ChannelFuture channelFuture;

    public NettyWebSocketServer(){

    }

    @Override
    public void run() {
        try {
            long begin = System.currentTimeMillis();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelHandler);
            long end = System.currentTimeMillis();
            logger.info("Netty Websocket服务器启动完成，耗时 " + (end - begin) + " ms，已绑定端口 " + port + " 阻塞式等候客户端连接");
            channelFuture = bootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            logger.info(e.getMessage());
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            e.printStackTrace();
        }
    }

    public void close(){
        channelFuture.channel().close();
        Future<?> bossGroupFuture = bossGroup.shutdownGracefully();
        Future<?> workerGroupFuture = workerGroup.shutdownGracefully();

        try {
            bossGroupFuture.await();
            workerGroupFuture.await();
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        }
    }

    public ChannelHandler getChildChannelHandler() {
        return channelHandler;
    }

    public void setChildChannelHandler(ChannelHandler childChannelHandler) {
        this.channelHandler = childChannelHandler;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
