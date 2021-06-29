package com.backend.demo.webSocket;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat/{sender}")
@Component
public class ChatEndpoint {

    private static Map<String, ChatEndpoint> onLineUsers = new ConcurrentHashMap<>() ;

    private Session session ;

    @OnOpen
    public void onOpen(Session session, @PathParam("sender") String sender) {
        this.session = session ;
        onLineUsers.put(sender, this) ;
    }

    @OnMessage
    public void onMessage(String message) {
        JSONObject jsonObject = new JSONObject(message);
        try {
            // 傳送訊息
            onLineUsers.get(jsonObject.getString("to")).session.
                    getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("web socket 關閉了...");
    }

}
