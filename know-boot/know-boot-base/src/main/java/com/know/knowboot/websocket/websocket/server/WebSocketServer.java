package com.know.knowboot.websocket.websocket.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint("/webInfo/{userId}")
@Component
public class WebSocketServer {

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId="";


    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) {
        this.session = session;
        this.userId=userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
            //加入set中
        }else{
            webSocketMap.put(userId,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        log.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());

        try {
            JSONObject object = new JSONObject();
            object.put("msgType","connect");
            object.put("content","连接成功");
            sendBaseMessage(object.toJSONString());
        } catch (IOException e) {
            log.error("用户:"+userId+",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {

        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNoneEmpty(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);

                // 判断消息类型 ping：心跳检测；message：消息
                String msgType = jsonObject.getString("msgType");
                if (msgType.equals("ping")){
                    JSONObject object = new JSONObject();
                    object.put("msgType","ping");
                    object.put("content","连接成功");
                    webSocketMap.get(userId).sendBaseMessage(object.toJSONString());
                }
                if (msgType.equals("message")){
//                    //追加发送人(防止串改)
//                    jsonObject.put("fromUserId",this.userId);
//                    String toUserId=jsonObject.getString("toUserId");
                    //传送给对应toUserId用户的websocket
                    if(StringUtils.isEmpty(userId)&&webSocketMap.containsKey(userId)){
                        webSocketMap.get(userId).sendBaseMessage(jsonObject.toJSONString());
                    }else{
                        log.error("请求的userId:"+userId+"不在该服务器上");
                        //否则不在这个服务器上，发送到mysql或者redis
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送(同步发送消息)
     */
    public void sendBaseMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 实现服务器主动推送(异步发送消息)
     */
    public void sendAsyncMessage(String message) throws IOException {
        this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.isEmpty(userId)&&webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendBaseMessage(message);
        }else{
            log.error("用户"+userId+",不在线！");
        }
    }

    /**
     * 群发信息的方法
     *
     * @param message 消息
     */
    public void sendMessageToAll(String message) {
        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                + "发送全体消息:" + message);
        //循环客户端map发送消息
        webSocketMap.values().forEach(item -> {
            //向每个用户发送文本信息。这里getAsyncRemote()解释一下，向用户发送文本信息有两种方式，
            // 一种是getBasicRemote，一种是getAsyncRemote
            //区别：getAsyncRemote是异步的，不会阻塞，而getBasicRemote是同步的，会阻塞，由于同步特性，第二行的消息必须等待第一行的发送完成才能进行。
            // 而第一行的剩余部分消息要等第二行发送完才能继续发送，所以在第二行会抛出IllegalStateException异常。所以如果要使用getBasicRemote()同步发送消息
            // 则避免尽量一次发送全部消息，使用部分消息来发送，可以看到下面sendMessageToTarget方法内就用的getBasicRemote，因为这个方法是根据用户id来私发的，所以不是全部一起发送。
            item.session.getAsyncRemote().sendText(message);
        });
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}