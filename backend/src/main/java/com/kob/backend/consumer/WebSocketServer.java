package com.kob.backend.consumer;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    private static UserMapper userMapper;//在WebSocket中注入不太一样，要加static，而且要把Autowired加到setter上

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }

    private static final CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();//线程安全的Set

    private static final ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();//用来存储所有的连接，对所有的实例可见，所以要改成static，
    //因为每个WebSocketServer实例在每个线程中，所以要用ConcurrentHashMap确保线程安全，来存储每个用户的ID以及对应的WebSocketServer实例
    private User user;
    private Session session;//每个用户的信息存到各自的WebSocket中的Session里面（不是HTTP Session）
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        log.info("Session connected!");
        Integer userId = JwtAuthentication.getUserId(token);//从token里读取userId
        this.user = userMapper.selectById(userId);
        if (this.user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        log.info("Session disconnected!");
        if(this.user != null){
            users.remove(this.user.getId());
            matchPool.remove(this.user);
        }
        // 关闭链接
    }

    public void startMatching(){
        log.info("Start matching!");
        matchPool.add(this.user);

        //临时调试用，之后会换成微服务
        while(matchPool.size() >= 2){
            Iterator<User> iterator = matchPool.iterator();
            User a = iterator.next();
            User b = iterator.next();
            matchPool.remove(a);
            matchPool.remove(b);

            Game game = new Game(13, 14, 40);
            game.createMap();
            JSONObject respA = new JSONObject();
            respA.put("event","start-matching");
            respA.put("opponent_username", b.getUsername());
            respA.put("opponent_photo", b.getPhoto());
            respA.put("gamemap",game.getGameMap());
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event","start-matching");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_photo", a.getPhoto());
            respB.put("gamemap",game.getGameMap());
            users.get(b.getId()).sendMessage(respB.toJSONString());

        }
    }
    public void stopMatching(){
        log.info("Stop matching!");
        matchPool.remove(this.user);
    }

    @OnMessage
    public void onMessage(String message, Session session) { //当做路由
        // 从Client接收消息
        log.info("receive message");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            startMatching();
        }else if("stop-matching".equals(event)){
            stopMatching();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message){
        synchronized (this.session){
            try {
                this.session.getBasicRemote().sendText(message); //服务器主动向客户端发送异步请求
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}