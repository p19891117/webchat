function SocketClient(url,uid,gids,open,close,accept){
	this.url = url;
	this.uid = uid;
	this.gids = gids;
	this.open = open;
	this.close= close;
	this.accept=accept;
	this.error = "";
	this.msgViewId = "view";
	this.ws = null;
	this.conn = function(){
		if(isNull(url)){
			error = "url不能为空";
			return;
		}
		if(isNull(uid)){
			error = "uid不能为空";
			return;
		}
		if(gids==null||gids.length<=0){
			error = "gids不能为空";
			return;
		}
		var id = BASE64.encoder(this.toJson({
			username:uid,
			groupids:gids
		}));
		var transports = {
			sessionId:function(){
				return id;
			}
		}
		ws = new SockJS(url, undefined, transports);
		ws.onopen = function() {
			open();
		};
		ws.onmessage = function(event) {
			var msg = event.data;
			accept(msg)
		};
		ws.onclose = function(event) {
			close(event);
		}
	}
	this.send = function(msg){
		if(ws==null)
			this.disconn();
		msg.username = uid;
		ws.send(this.toJson(msg));
	}
	this.isNull = function(msg){
		if (!msg || msg.trim().length == 0) 
			return true;
		return false;
	}
	this.toJson= function(obj){
		return JSON.stringify(obj);
	}
	this.viewMsg=function(msg){
		var msgJq = $("#"+this.msgViewId);
		msgJq.val(isNull(msgJq.val())?msg:msgJq.val()+"\n"+msg);
	}
	this.disconn=function(){
		if (ws != null) {
			ws.close();
			ws = null;
			this.viewMsg("断开服务器");
		}
	}
}