package com.example.myblog.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myblog.domain.ChatFriends;
import com.example.myblog.exception.constant.ChatMap;
import com.example.myblog.exception.constant.ChatType;
import com.example.myblog.exception.constant.GlobalConstant;
import com.example.myblog.exception.constant.ResponseJson;
import com.example.myblog.mapper.ChatFriendsMapper;
import com.example.myblog.service.ChatService;
import com.example.myblog.vo.ChatUserVo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SJC
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ChatFriendsMapper chatFriendsMapper;

    @Override
    public void register(JSONObject param, ChannelHandlerContext ctx) {
        String username = (String)param.get("username");
        redisTemplate.opsForSet().add(GlobalConstant.ONLINE_USER_REDIS_KEY +username,
                JSON.toJSONString(ctx));
        ChatMap.onlineUserMap.put(username,ctx);
        String responseJson = new ResponseJson().success()
                .setData("type", ChatType.REGISTER)
                .toString();
        sendMessage(ctx, responseJson);
    }

    @Override
    public void singleSend(JSONObject param, ChannelHandlerContext ctx) { String fromUserId = (String)param.get("form");
        String toUsername = (String)param.get("to");
        String content = (String)param.get("message");
        String s =
                redisTemplate.opsForSet().randomMember(GlobalConstant.ONLINE_USER_REDIS_KEY + toUsername);
        ChannelHandlerContext toUserCtx = JSON.parseObject(s,
                ChannelHandlerContext.class);
        if (toUserCtx == null) {
            String responseJson = new ResponseJson()
                    .error(MessageFormat.format("userId为 {0} 的用户没有登录！", toUsername))
                    .toString();
            sendMessage(ctx, responseJson);
        } else {
            String responseJson = new ResponseJson().success()
                    .setData("form", fromUserId)
                    .setData("message", content)
                    .setData("type", ChatType.SINGLE_SENDING)
                    .toString();
            sendMessage(toUserCtx, responseJson);
        }
    }

    @Override
    public void remove(ChannelHandlerContext ctx) {

    }

    @Override
    public void typeError(ChannelHandlerContext ctx) {
        String responseJson = new ResponseJson()
                .error("该类型不存在！")
                .toString();
        sendMessage(ctx, responseJson);
    }

    @Override
    public ResponseJson loadUserInfo(String username) {
        List<ChatFriends> chatFriends =
                chatFriendsMapper.selectList(new QueryWrapper<ChatFriends>().eq(
                "username", username));
        ChatUserVo chatUserVo = new ChatUserVo();
        chatUserVo.setUsername(username);
        List<String> friends = new ArrayList<>();
        for (ChatFriends chatFriend : chatFriends) {
            friends.add(chatFriend.getFriend());
        }
        chatUserVo.setFriends(friends);
        return new ResponseJson().success().setData("userInfo",chatUserVo);
    }

    @Override
    public void bindFriend(String username, String friend) {
        //双向绑定，加载好友列表的时候双方都能加载出来。
        ChatFriends chatFriends = new ChatFriends();
        chatFriends.setFriend(friend);
        chatFriends.setUsername(username);
        ChatFriends chatFriends1 = new ChatFriends();
        chatFriends1.setFriend(username);
        chatFriends1.setUsername(friend);
        ChatFriends selectOne =
                chatFriendsMapper.selectOne(new QueryWrapper<ChatFriends>().eq("username",
                username)
                .eq("friend", friend));
        if (selectOne!=null){
        }else {
            chatFriendsMapper.insert(chatFriends1);
            chatFriendsMapper.insert(chatFriends);
        }
    }

    private void sendMessage(ChannelHandlerContext ctx, String message) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
    }
}
