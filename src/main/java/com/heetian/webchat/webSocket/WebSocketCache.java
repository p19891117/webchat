package com.heetian.webchat.webSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.heetian.webchat.exception.GroupException;
import com.heetian.webchat.exception.UserException;

/**
 * 用户信息缓存
 * @author peiyu
 */
public class WebSocketCache {
    private List<WebSocketGroup> cache = new ArrayList<WebSocketGroup>();
    /*{	
    	cache.add(new WebSocketGroup(WebSocketGroup.defaultg));
    }*/
    private static WebSocketCache me = new WebSocketCache();
    private WebSocketCache(){}
    public static WebSocketCache me() {
        return me;
    }

    public void connection(String groupName,String username, WebSocketSession session) throws GroupException, UserException {
    	if(StringUtils.isBlank(groupName))
    		throw new GroupException("群名必须");
    	if(StringUtils.isBlank(username))
    		throw new UserException("用户名必须");
    	session.getAttributes().put("groupName", groupName);
    	session.getAttributes().put("username", username);
    	boolean flag = true;
    	for(WebSocketGroup group :cache){
    		if(!group.getGroupName().equals(groupName))
    			continue;
    		flag = false;
    		for(WebSocketExt ses:group.all()){
    			if(ses.getUsername().equals(username))
    				throw new UserException("用户名["+username+"]已经登录");
    		}
    		group.addSeesion(new WebSocketExt(username, session));
    	}
    	if(flag){
    		WebSocketGroup group = new WebSocketGroup(groupName);
    		group.addSeesion(new WebSocketExt(username, session));
    		cache.add(group);
    	}
    }
    public List<WebSocketExt> getAll() {
    	List<WebSocketExt> sessions = new ArrayList<WebSocketExt>();
    	for(WebSocketGroup g:cache){
    		sessions.addAll(g.all());
    	}
    	return sessions;
    }
    public void disconnection(String sid){
    	this.cache.stream().forEach(wsg->{
    		wsg.all().removeIf(wse->wse.getSessionID().equals(sid));
    	});
    }
    public void disconnection(String group,String username) throws GroupException, UserException {
    	if(StringUtils.isBlank(group))
    		throw new GroupException("群名必须");
    	if(StringUtils.isBlank(username))
    		throw new UserException("用户名必须");
    	cache.stream().filter(wsg->wsg.getGroupName().equals(group)).forEach(wsg->{
    		wsg.all().removeIf(wse->wse.getUsername().equals(username));
    	});
    }
    public void sendAll(WebSocketMessage<?> message) throws IOException{
    	cache.stream()
    		.flatMap(wsg->wsg.all().stream())
    		.filter(wse->wse!=null)
    		.forEach(wse->{wse.sendMessage(message);});
    }
    public void sendGroup(String group,WebSocketMessage<?> message) throws IOException, GroupException{
    	if(StringUtils.isBlank(group))
    		throw new GroupException("群名必须");
    	List<WebSocketGroup> wsgs = cache.stream().filter(wsg->wsg.getGroupName().equals(group)).collect(Collectors.toList());
    	if(wsgs==null||wsgs.size()<=0){
    		throw new GroupException("群名["+group+"]不存在");
    	}
    	WebSocketGroup wsg = wsgs.remove(0);
    	wsg.all().stream().forEach(wse->{wse.sendMessage(message);});
    }
    public void sendUser(String username,String group,WebSocketMessage<?> message) throws IOException, GroupException, UserException{
    	if(StringUtils.isBlank(group))
    		throw new GroupException("群名必须");
    	if(StringUtils.isBlank(username))
    		throw new UserException("用户名必须");
    	List<WebSocketGroup> wsgs = cache.stream().filter(wsg->wsg.getGroupName().equals(group)).collect(Collectors.toList());
    	if(wsgs==null||wsgs.size()<=0){
    		throw new GroupException("群名["+group+"]不存在");
    	}
    	WebSocketGroup wsg = wsgs.remove(0);
    	
    	List<WebSocketExt> wses = wsg.all().stream().filter(wse->wse.getUsername().equals(username)).collect(Collectors.toList());
    	if(wses==null||wses.size()<=0){
    		throw new GroupException("用户名["+username+"]不存在");
    	}
    	WebSocketExt wse = wses.remove(0);
    	wse.sendMessage(message);
    }
}
