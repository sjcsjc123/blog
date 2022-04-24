package com.example.myblog.config.handler;

import com.example.myblog.exception.constant.ChatMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author SJC
 */
@Component
@ChannelHandler.Sharable
public class HttpRequestHandler extends SimpleChannelInboundHandler<Object> {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                Object o) throws Exception {
        if (o instanceof FullHttpRequest){
            FullHttpRequest httpRequest = (FullHttpRequest) o;
            if (!httpRequest.decoderResult().isSuccess()){
                DefaultFullHttpResponse httpResponse =
                        new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                        HttpResponseStatus.BAD_REQUEST);
                if (httpResponse.status().code() != 200){
                    ByteBuf byteBuf =
                            Unpooled.copiedBuffer(httpResponse.status().toString(),
                            CharsetUtil.UTF_8);
                    httpResponse.content().writeBytes(byteBuf);
                    byteBuf.release();
                }
                boolean keepAlive = HttpUtil.isKeepAlive(httpRequest);
                ChannelFuture channelFuture =
                        channelHandlerContext.channel().writeAndFlush(httpResponse);
                if (!keepAlive){
                    channelFuture.addListener(ChannelFutureListener.CLOSE);
                }
                return;
            }
            WebSocketServerHandshakerFactory socketServerHandshakerFactory = new WebSocketServerHandshakerFactory("ws:/" + channelHandlerContext.channel() +
                    "/websocket", null, false);
            WebSocketServerHandshaker serverHandShaker =
                    socketServerHandshakerFactory.newHandshaker(httpRequest);
            //存储握手实例
            ChatMap.webSocketHandShakerMap.put(channelHandlerContext.channel().id().asLongText(),serverHandShaker);
            if (serverHandShaker==null){
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(channelHandlerContext.channel());
            }else {
                serverHandShaker.handshake(channelHandlerContext.channel(),
                        httpRequest);
            }
        }
        else if (o instanceof WebSocketFrame){
            channelHandlerContext.fireChannelRead(((WebSocketFrame) o).retain());
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
