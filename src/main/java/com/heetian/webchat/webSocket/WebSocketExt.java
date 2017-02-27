package com.heetian.webchat.webSocket;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.heetian.webchat.bean.WebSocketStatus;

public class WebSocketExt {
	public static final String USERNAME = "username_key_session";
	private static final Logger log = LoggerFactory.getLogger(WebSocketExt.class);
	private WebSocketSession session;
	private WebSocketStatus status = WebSocketStatus.NOR;
	private String errMsg ;
	
	public WebSocketExt(String username, WebSocketSession session) {
		super();
		this.session = session;
		this.session.getAttributes().put(USERNAME, username);
	}

	public WebSocketSession getSession() {
		return session;
	}
	public void setSession(WebSocketSession session) {
		this.session = session;
	}
	public String getSessionID() {
		return this.session.getId();
	}
	public String getUsername() {
		return (String) this.session.getAttributes().get(USERNAME);
	}
	
	public WebSocketStatus getStatus() {
		return status;
	}

	public void setStatus(WebSocketStatus status) {
		this.status = status;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public void sendMessage(WebSocketMessage<?> message){
		try {
			session.sendMessage(message);
		} catch (IOException e) {
			errMsg = e.getMessage();
			status = WebSocketStatus.ERR;
			log.warn("发送消息：用户["+getUsername()+"]发送失败;关闭该连接",e);
			try {
				session.close();
			} catch (IOException e1) {
				log.warn("发送消息：用户["+getUsername()+"]关闭连接失败",e);
			}
		}
	}
}
