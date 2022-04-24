package com.example.myblog.config.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author SJC
 */
@Component
public class NettyChildHandler extends ChannelInitializer<SocketChannel> {

    @Resource(name = "httpRequestHandler")
    private ChannelHandler httpRequestHandler;
    @Resource(name = "webSocketFrameHandler")
    private ChannelHandler webSocketFrameHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new HttpServerCodec());
        socketChannel.pipeline().addLast(new HttpObjectAggregator(65536));
        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
        socketChannel.pipeline().addLast(httpRequestHandler);
        socketChannel.pipeline().addLast(webSocketFrameHandler);
    }
}
