package com.heetian.webchat.webSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.WebSocketMessage;

import com.heetian.webchat.exception.UserException;

public class WebSocketGroup {
	public static final String defaultg = "default_group_no_group_user";
	private String groupName;
	private List<WebSocketExt> sessions = new ArrayList<WebSocketExt>();
	public WebSocketGroup() {
	}
	public WebSocketGroup(String groupName) {
		super();
		this.groupName = groupName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public void addSeesion(WebSocketExt sex){
		if(sex==null)
			return;
		if(StringUtils.isBlank(sex.getUsername()))
			return;
		for(WebSocketExt ca:sessions){
			if(ca.getUsername().equals(sex.getUsername()))
				return;
		}
		sessions.add(sex);
	}
	public void delete(String username){
		if(StringUtils.isBlank(username))
			return;
		Iterator<WebSocketExt> iter = sessions.iterator();
    	while(iter.hasNext()){
    		if(iter.next().getUsername().equals(username))
    			iter.remove();
    	}
	}
	public List<WebSocketExt> all(){
		return sessions;
	}
	public void sendGroup(WebSocketMessage<?> message) throws IOException{
		for(WebSocketExt ss:sessions){
			ss.sendMessage(message);
		}
	}
	public void sendUser(String username,WebSocketMessage<?> message) throws IOException, UserException{
		boolean flag = true;
		for(WebSocketExt ss:sessions){
			if(ss.getUsername().equals(username)){
				flag = false;
				ss.sendMessage(message);
			}
		}
		if(flag){
			throw new UserException("用户名["+username+"]不存在,或者未上线");
		}
	}
}
