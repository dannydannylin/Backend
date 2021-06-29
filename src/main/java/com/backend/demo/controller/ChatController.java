package com.backend.demo.controller;

import com.backend.demo.domain.Friend;
import com.backend.demo.domain.Message;
import com.backend.demo.po.User;
import com.backend.demo.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/controller")
public class ChatController {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService ;

    // 設定每個用戶可以聊天的對象
    @GetMapping("/chatList/{loginUser}/{chatMan}")
    public boolean addChatList(@PathVariable String loginUser,
                               @PathVariable String chatMan) {

        // 自己跟自己聊天 ???
        if ( loginUser.equals(chatMan) )
            return false ;

        // 在 login user 中 add 進 chatMan
        redisTemplate.opsForSet().add(loginUser + "-chat", chatMan) ;

        // 在 chatMan 中 add 進 login user
        redisTemplate.opsForSet().add(chatMan + "-chat", loginUser) ;

        return true ;
    }

    // 拿取此用戶可以和哪些聊天
    @GetMapping("/chatMenInfo/{account}")
    public Set<Friend> getChatMenInfo(@PathVariable String account) {

        // 拿聊天成員
        Set<String> members = redisTemplate.opsForSet().members(account + "-chat");
        // 回傳值
        Set<Friend> re = new HashSet<>() ;

        // 封裝
        // friends: [
        //                {
        //                    account: "t",
        //                    lastChat: "123",
        //                    lastChatTime: "5/22 , 22:39",
        //                    absoluteTime:
        //                    allMessages:[
        //                    ]
        //                }
        //          ]
        for ( String member : members ) {
            Friend friend = new Friend();
            // 聊天對象
            friend.setAccount(member);
            // 聊對話訊息
            List<Message> messages =
                    (List<Message>)
                            redisTemplate.opsForList().range(account + "-->" + member, 0, -1);

            // 最後一句對話
            // 最後一句對話的時間
            if ( messages != null
                    && messages.size() > 0 ) {
                friend.setMessages(messages);
                friend.setLastChat(messages.get(messages.size()-1).getMessage());
                friend.setLastChatTime(messages.get(messages.size()-1).getTime());
                friend.setLastAbsoluteTime(messages.get(messages.size()-1).getAbsoluteTime());
            }
            else {
                // 放一個空的 list
                List<Message> messageArrayList = new ArrayList<>();
                messageArrayList.add(new Message());
                friend.setMessages(messageArrayList);
                friend.setLastChat("") ;
                friend.setLastChatTime(null);
                friend.setLastAbsoluteTime(null);
            }

            re.add(friend) ;
        }

        return re ;
    }

    // 存聊天訊息
    @PostMapping("/chatMessage/{sender}/{accepter}")
    public boolean saveChatMessage(@RequestBody Message messages,
                                   @PathVariable String sender,
                                   @PathVariable String accepter) {

        // 自己跟自己聊天 ???
        if ( sender.equals(accepter) )
            return false ;

        // 存訊息
        // 一直往裡面推
        // 右邊推 左邊拿
        redisTemplate.opsForList().rightPush(sender + "-->" + accepter, messages);

        // 兩個方向的都要存
        messages.setDirection("incoming");
        redisTemplate.opsForList().rightPush(accepter + "-->" + sender, messages);

        return true ;
    }

    // 刪除聊天訊息
    @DeleteMapping("/chatMessage/{sender}/{accepter}")
    public boolean deleteChatMessage(@PathVariable String sender,
                                     @PathVariable String accepter) {

        // 刪自己 ???
        if ( sender.equals(accepter) )
            return false ;

        // 只能單方面的刪除聊天紀錄
        redisTemplate.delete(sender + "-->" + accepter) ;

        return true ;
    }

    // 搜尋好友
    @PostMapping("/searchFriends")
    public List<String> searchFriends(@RequestBody String Text) {

        JSONObject jsonObject = new JSONObject(Text) ;
        String searchText = jsonObject.getString("searchText");

        List<User> users = userService.getAllUserBySearch(searchText);
        List<String> re = new ArrayList<>();

        for (User user : users) {
            re.add(user.getAccount()) ;
        }

        return re ;
    }

    // 改為已讀
    // 不可能改為未讀
    // 應該說我沒做這個功能啦!
    @PostMapping("/read/{sender}/{accepter}")
    public boolean read(@PathVariable String sender,
                        @PathVariable String accepter) {

        List<Message> messages =
                (List<Message>)
                        redisTemplate.opsForList().range(sender + "-->" + accepter, 0, -1);

        // 將最後一個訊息設為已讀
        Message message = messages.get(messages.size() - 1);
        message.setRead(true);

        redisTemplate.opsForList().set(sender + "-->" + accepter,
                messages.size()-1, message);

        return true ;
    }

    // 判斷對話是否已讀
    @GetMapping("/read/{sender}/{accepter}")
    public boolean isRead(@PathVariable String sender,
                          @PathVariable String accepter) {

        List<Message> messages =
                (List<Message>)
                        redisTemplate.opsForList().range(sender + "-->" + accepter, 0, -1);

        return messages.get(messages.size()-1).isRead();
    }

}




