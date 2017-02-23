package com.heetian.webchat.bean;

import com.heetian.webchat.webSocket.WebSocketGroup;

/**
 * 消息格式：
 	
 	username, 必须
 	group, 必须
 	touser,
 	content,
 	create, true 为创建连接;连接后第一次发送时必须。
 	destType; 是发给哪类对象的，0:群组 1:群组成员
 	
 
 
 * @author tst
 *
 */
public class Msg {
	private String username;
	private String group = WebSocketGroup.defaultg;
	private String touser;
	private String content;
	private boolean create = false;
	private int destType;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isCreate() {
		return create;
	}
	public void setCreate(boolean create) {
		this.create = create;
	}
	public int getDestType() {
		return destType;
	}
	public void setDestType(int destType) {
		this.destType = destType;
	}
}
