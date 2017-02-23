package com.heetian.webchat.webSocket;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.heetian.webchat.bean.Msg;
import com.heetian.webchat.exception.GroupException;
import com.heetian.webchat.exception.UserException;
import com.heetian.webchat.exception.UserTypeException;
import com.heetian.webchat.utils.DateUtils;

/**
 * websocket消息处理器类
 * @author peiyu
 */
public class EchoHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(EchoHandler.class);

    //登录者信息缓存，主要用于把session id和昵称关联起来，用于发送消息
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload().trim();
        if(StringUtils.isBlank(msg))
        	return;
        StringBuilder sb = new StringBuilder();
        try {
        	Msg msgBean = new Gson().fromJson(msg, Msg.class);
        	sb.append("用户["+msgBean.getUsername()+"]");
        	//用于限制用户发送信息的频率，防止恶意刷屏
	        if(msgBean.isCreate()){
	        	log.debug("{}上线!`sessionid:{}",msgBean.getUsername(), session.getId());
	        	sb.append("上线啦!");
	            WebSocketCache.me().connection(msgBean.getGroup(),msgBean.getUsername(), session);
	          //新创建的websocket,通知系统所有人。
	        	WebSocketCache.me().sendAll(message(sb.toString()));
	        }else{
	        	sb.append("说：" + msgBean.getContent());
	        	//非创建型，判断是发给群组还是发给群组内单个人。
	        	String group = msgBean.getGroup();
	        	switch (msgBean.getDestType()) {
				case 0:
					//发给群组
					WebSocketCache.me().sendGroup(group, message(sb.toString()));
					break;
				case 1:
					//发给群组内单个人;判断目标人员是否在先，在线与不在线通过异常完成
					WebSocketCache.me().sendUser(msgBean.getTouser(), group, message(sb.toString()));
					break;
				default:
					throw new UserTypeException(" 转发类型出错只能为0:群组 1:群组成员");
				}
	        }
        }catch (JsonSyntaxException e) {
        	session.sendMessage(message(sb.toString()+" " + "消息格式不对"+msg));
		}catch (GroupException|UserException|UserTypeException e) {
        	session.sendMessage(message(sb.toString()+" " +e.getMessage()));
		}catch (Exception e) {
            log.error("发送异常:", e);
        }
    }
	public TextMessage message(String content){
		String result = DateUtils.date2String(new Date()) + " " + content;
		log.debug("回复内容:{}", result);
		return new TextMessage(result);
	}
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.debug("建立链接");
        //在这里发现了一个从session中获取额外参数的方法，但是在前端还没找到方法可以将数据放进这个里面，如果可以将昵称放进去，那么就不用那么蛋疼的记录登录者的昵称了
        Map<String, Object> attributes = session.getAttributes();
        if (null == attributes || attributes.isEmpty()) {
            log.debug("没有参数....");
        } else {
            attributes.forEach((key, value) -> log.debug("key:{}, value:{}", key, value));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.debug("关闭链接");
        WebSocketCache.me().disconnection(session.getId());
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(session.getId() + "下线啦!"));
        }
        WebSocketCache.me().getAll().forEach(s -> {
            try {
                String now = DateUtils.date2String(new Date());
                s.sendMessage(new TextMessage(now + " " + session.getId() + "下线啦!"));
            } catch (IOException e) {
                log.error("异常", e);
            }
        });
    }


    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        log.debug("handleBinaryMessage:{}", message.toString());
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.debug("handlePongMessage:{}", message.toString());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("handleTransportError:", exception);
        WebSocketCache.me().disconnection(session.getId());
        if(session.isOpen()) {
            session.close();
        }
        WebSocketCache.me().getAll().forEach(s -> {
            try {
                String now = DateUtils.date2String(new Date());
                s.sendMessage(new TextMessage(now + " " + session.getId() + "下线啦!"));
            } catch (IOException e) {
                log.error("异常", e);
            }
        });
    }
}
