package com.heetian.webchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.heetian.webchat.webSocket.WebSocketCache;

/**
 * @author peiyu
 */
@Controller
@RequestMapping(value = "chat")
public class ChatController {
    /**
     * 轮询获取当前在线用户数
     * @return 在线用户数
     */
    @ResponseBody
    @RequestMapping(value = "/getSum", method = RequestMethod.GET)
    public String getSum() {
        int size = WebSocketCache.me().getAll().size();
        return String.valueOf(size);
    }
}
