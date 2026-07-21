//package com.know.knowboot.websocket.tio.config;
//
//import com.know.knowboot.websocket.tio.server.TioWsMsgHandler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.tio.server.ServerTioConfig;
//import org.tio.websocket.server.WsServerStarter;
//
//import java.io.IOException;
//
//@Configuration
//public class TioWebSocketConfig {
//
//    /**
//     * 注入消息处理器
//     */
//    @Autowired
//    private TioWsMsgHandler myWsMsgHandler;
//
//    /**
//     * 启动类配置
//     *
//     * @return
//     * @throws IOException
//     */
//    @Bean
//    public WsServerStarter wsServerStarter() throws IOException {
//        // 设置处理器
//        WsServerStarter wsServerStarter = new WsServerStarter(6789, myWsMsgHandler);
//        // 获取到ServerTioConfig
//        ServerTioConfig serverTioConfig = wsServerStarter.getServerTioConfig();
//        // 设置心跳超时时间，默认：1000 * 120
//        serverTioConfig.setHeartbeatTimeout(1000 * 120);
//        // 启动
//        wsServerStarter.start();
//        return wsServerStarter;
//    }
//
//}
