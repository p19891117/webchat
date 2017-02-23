package com.heetian.webchat.webSocket;

import java.io.IOException;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketExt {
	private  String username;
	private WebSocketSession session;
	
	public WebSocketExt() {
		super();
	}
	
	public WebSocketExt(String username, WebSocketSession session) {
		super();
		this.username = username;
		this.session = session;
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
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void sendMessage(WebSocketMessage<?> message) throws IOException{
		session.sendMessage(message);
	}
}
