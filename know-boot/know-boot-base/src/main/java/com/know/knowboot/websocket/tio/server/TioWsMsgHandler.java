//package com.know.knowboot.websocket.tio.server;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.tio.core.ChannelContext;
//import org.tio.core.Tio;
//import org.tio.http.common.HttpRequest;
//import org.tio.http.common.HttpResponse;
//import org.tio.websocket.common.WsRequest;
//import org.tio.websocket.common.WsResponse;
//import org.tio.websocket.server.handler.IWsMsgHandler;
//
//@Slf4j
//@Component
//public class TioWsMsgHandler implements IWsMsgHandler {
//
//    /**
//     * 对httpResponse参数进行补充并返回，如果返回null表示不想和对方建立连接，框架会断开连接，如果返回非null，框架会把这个对象发送给对方
//     * 注：请不要在这个方法中向对方发送任何消息，因为这个时候握手还没完成，发消息会导致协议交互失败。
//     * 对于大部分业务，该方法只需要一行代码：return httpResponse;
//     * @param httpRequest
//     * @param httpResponse
//     * @param channelContext
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
//
//        log.info("---------------------------- 对httpResponse参数进行补充并返回 -------------------------------------");
//
//        // 可以在此做一些业务逻辑，返回null表示不想连接
//        return httpResponse;
//    }
//
//    /**
//     * 握手成功后触发该方法
//     * @param httpRequest
//     * @param httpResponse
//     * @param channelContext
//     * @throws Exception
//     */
//    @Override
//    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
//
//        log.info("---------------------------- 握手成功后触发该方法 -------------------------------------");
//
//        // 拿到用户id
//        String id = httpRequest.getParam("id");
//        // 绑定用户
//        Tio.bindUser(channelContext, id);
//        // 给用户发送消息
//        WsResponse wsResponse = WsResponse.fromText("如果您看到此消息，表示您已成功连接 WebSocket 服务器", "UTF-8");
//        Tio.sendToUser(channelContext.tioConfig, id, wsResponse);
//    }
//
//    /**
//     * 收到Opcode.BINARY消息时，执行该方法。也就是说如何你的ws是基于BINARY传输的，就会走到这个方法
//     * @param wsRequest
//     * @param bytes
//     * @param channelContext
//     * @return 可以是WsResponse、byte[]、ByteBuffer、String或null，如果是null，框架不会回消息
//     * @throws Exception
//     */
//    @Override
//    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
//
//        log.info("---------------------------- 收到Opcode.BINARY消息时，执行该方法 -------------------------------------");
//
//        return null;
//    }
//
//    /**
//     * 收到Opcode.CLOSE时，执行该方法，业务层在该方法中一般不需要写什么逻辑，空着就好
//     * @param wsRequest
//     * @param bytes
//     * @param channelContext
//     * @return 可以是WsResponse、byte[]、ByteBuffer、String或null，如果是null，框架不会回消息
//     * @throws Exception
//     */
//    @Override
//    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
//
//        log.info("---------------------------- 收到Opcode.CLOSE时，执行该方法 -------------------------------------");
//
//        // 关闭连接
//        Tio.remove(channelContext, "WebSocket Close");
//        return null;
//    }
//
//    /**
//     * 当收到Opcode.TEXT消息时，执行该方法。也就是说如何你的ws是基于TEXT传输的，就会走到这个方法
//     * @param wsRequest
//     * @param text
//     * @param channelContext
//     * @return 可以是WsResponse、byte[]、ByteBuffer、String或null，如果是null，框架不会回消息
//     * @throws Exception
//     */
//    @Override
//    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
//
//        log.info("---------------------------- 当收到Opcode.TEXT消息时，执行该方法 -------------------------------------");
//
//        WsResponse wsResponse = WsResponse.fromText("服务器已收到消息：" + text, "UTF-8");
//        Tio.sendToUser(channelContext.tioConfig, channelContext.userid, wsResponse);
//        return null;
//
//    }
//
//}
