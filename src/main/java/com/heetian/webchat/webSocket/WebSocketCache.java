package com.heetian.webchat.webSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    {	
    	cache.add(new WebSocketGroup(WebSocketGroup.defaultg));
    }
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
    	Iterator<WebSocketGroup> giter = cache.iterator();
    	while(giter.hasNext()){
    		WebSocketGroup wsg = giter.next();
    		Iterator<WebSocketExt> eiter = wsg.all().iterator();
    		while(eiter.hasNext()){
    			WebSocketExt e = eiter.next();
    			if(e.getSessionID().equals(sid)){
    				eiter.remove();
    			}
    		}
    	}
    }
    public void disconnection(String group,String username) throws GroupException, UserException {
    	if(StringUtils.isBlank(group))
    		throw new GroupException("群名必须");
    	if(StringUtils.isBlank(username))
    		throw new UserException("用户名必须");
    	boolean flag = true;
    	Iterator<WebSocketGroup> giter = cache.iterator();
    	while(giter.hasNext()){
    		WebSocketGroup wsg = giter.next();
    		if(!wsg.getGroupName().equals(group))
    			continue;
    		flag = false;
    		Iterator<WebSocketExt> eiter = wsg.all().iterator();
    		boolean uflag = true;
    		while(eiter.hasNext()){
    			WebSocketExt e = eiter.next();
    			if(e.getUsername().equals(username)){
    				eiter.remove();
    				uflag = false;
    			}
    		}
    		if(uflag){
    			throw new UserException("用户名["+username+"]不存在");
    		}
    	}
    	if(flag){
    		throw new GroupException("群名["+group+"]不存在");
    	}
    }
    public void sendAll(WebSocketMessage<?> message) throws IOException{
    	for(WebSocketExt wse:getAll()){
    		wse.sendMessage(message);
    	}
    }
    public void sendGroup(String group,WebSocketMessage<?> message) throws IOException, GroupException{
    	if(StringUtils.isBlank(group))
    		throw new GroupException("群名必须");
    	boolean flag = true;
    	for(WebSocketGroup wsg:cache){
    		if(wsg.getGroupName().equals(group)){
    			flag = false;
    			wsg.sendGroup(message);
    		}
    	}
    	if(flag){
    		throw new GroupException("群名["+group+"]不存在");
    	}
    }
    public void sendUser(String username,String group,WebSocketMessage<?> message) throws IOException, GroupException, UserException{
    	if(StringUtils.isBlank(group))
    		throw new GroupException("群名必须");
    	if(StringUtils.isBlank(username))
    		throw new UserException("用户名必须");
    	boolean flag = true;
    	for(WebSocketGroup wsg:cache){
    		if(wsg.getGroupName().equals(group)){
    			flag = false;
    			wsg.sendUser(username, message);
    		}
    	}
    	if(flag){
    		throw new GroupException("群名["+group+"]不存在");
    	}
    }
}
