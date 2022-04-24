package com.example.myblog.config.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.myblog.exception.constant.ChatMap;
import com.example.myblog.exception.constant.ResponseJson;
import com.example.myblog.service.ChatService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author SJC
 */
@Component
@ChannelHandler.Sharable
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ChatService chatService;
    private final Logger logger =
            LoggerFactory.getLogger(WebSocketFrameHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                WebSocketFrame webSocketFrame) throws Exception {
        if (webSocketFrame instanceof CloseWebSocketFrame){
            WebSocketServerHandshaker serverHandShaker =
                    ChatMap.webSocketHandShakerMap.get(channelHandlerContext.channel().id().asLongText());
            if (serverHandShaker==null){
                sendErrorMessage(channelHandlerContext, "不存在的客户端连接！");
            }else {
                serverHandShaker.close(channelHandlerContext.channel(),
                        (CloseWebSocketFrame) webSocketFrame.retain());
            }
            return;
        }
        if (webSocketFrame instanceof PingWebSocketFrame){
            channelHandlerContext.channel().write(new PongWebSocketFrame(webSocketFrame.content().retain()));
            return;
        }
        if (!(webSocketFrame instanceof TextWebSocketFrame)){
            sendErrorMessage(channelHandlerContext, "仅支持文本(Text)格式，不支持二进制消息");
        }
        String request = ((TextWebSocketFrame) webSocketFrame).text();
        logger.info("服务端收到新信息：" + request);
        JSONObject param = null;
        try {
            param = JSONObject.parseObject(request);
        } catch (Exception e) {
            sendErrorMessage(channelHandlerContext, "JSON字符串转换出错！");
            e.printStackTrace();
        }
        if (param == null) {
            sendErrorMessage(channelHandlerContext, "参数为空！");
            return;
        }
        String type = (String) param.get("type");
        switch (type) {
            case "REGISTER":
                chatService.register(param, channelHandlerContext);
                break;
            case "SINGLE_SENDING":
                chatService.singleSend(param, channelHandlerContext);
                break;
            default:
                chatService.typeError(channelHandlerContext);
                break;
        }
    }

    private void sendErrorMessage(ChannelHandlerContext ctx, String errorMsg) {
        String responseJson = new ResponseJson()
                .error(errorMsg)
                .toString();
        ctx.channel().writeAndFlush(new TextWebSocketFrame(responseJson));
    }

    /**
     * 异常处理：关闭channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
