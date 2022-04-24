package com.example.myblog.service;

import com.alibaba.fastjson.JSONObject;
import com.example.myblog.exception.constant.ResponseJson;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author SJC
 */
public interface ChatService {

    /**
     * 注册
     * @param param
     * @param ctx
     */
    void register(JSONObject param, ChannelHandlerContext ctx);

    /**
     * 私聊
     * @param param
     * @param ctx
     */
    void singleSend(JSONObject param, ChannelHandlerContext ctx);

    /**
     * 移除
     * @param ctx
     */
    void remove(ChannelHandlerContext ctx);

    /**
     * 类型发生错误
     * @param ctx
     */
    void typeError(ChannelHandlerContext ctx);

    /**
     * 加载好友列表
     * @param username
     * @return
     */
    ResponseJson loadUserInfo(String username);

    /**
     * 绑定好友
     * @param username
     * @param friend
     */
    void bindFriend(String username,String friend);
}
