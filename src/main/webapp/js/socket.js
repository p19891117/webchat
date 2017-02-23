$(function() {
	setInterval("getSum()", 10000);
})
$("#destType").change(function(){
	if($(this).val() == "1"){
		$("#touser").attr("disabled",false);
	}else{
		$("#touser").attr("disabled","disabled");
	}
});
// 目前是通过不停的轮询的方式获取在线人数的，还没想到更好的办法
function getSum() {
	$.ajax({
		url :"chat/getSum",
		type : 'get',
		success : function(data, status) {
			var temp = data;
			$("#sum").html("在线人数：" + temp);
		},
		error : function(data, status, e) {
		}
	});
}
var ws = null;
var url = null;
var transports = [];
var now = null;

function connect() {
	var host = window.location.host;
	url = 'http://' + window.location.host + '/nsrep-webchat/sockjs/echo';
	if (!url) {
		alert('Select whether to use W3C WebSocket or SockJS');
		return;
	}
	log(url);
	var name = document.getElementById('username').value;
	if(isNull(name)){
		alert('请输入昵称!')
		return;
	}
	var group = document.getElementById('groupname').value;
	if(isNull(group)){
		alert('请输入群组!')
		return;
	}
	var json = JSON.stringify({
		username:name,
		group:group,
		create:true,
		});
	ws = new SockJS(url, undefined, transports);
	ws.onopen = function() {
		document.getElementById('msg').focus();
		document.getElementById('msg').disabled=false
		document.getElementById('conn').disabled="disabled";
		document.getElementById('disconn').disabled=false;
		document.getElementById('send').disabled=false;
		document.getElementById('destType').disabled=false;
		// 通过发送一条带有特殊符号的信息告诉后台登陆者的昵称，很蛋疼
		// 最好是可以在创建连接时就将这个信息一并带回服务器的，但是我还没找到可以用的这样的方法
		ws.send(json);
		log('服务器连接成功!');
	};
	ws.onmessage = function(event) {
		log(event.data);
	};
	ws.onclose = function(event) {
		document.getElementById('msg').disabled="disabled";
		document.getElementById('conn').disabled=false;
		document.getElementById('disconn').disabled="disabled";
		document.getElementById('send').disabled="disabled";
		document.getElementById('destType').disabled="disabled";
		if (event.code == 1007) {
			log('小样，昵称太长了!');
		}
		log('断开连接!');
	};
}
function isNull(msg){
	if (!msg || msg.trim().length == 0) 
		return true;
	return false;
}
function disconnect() {
	/*document.getElementById('conn').disabled=null;
	document.getElementById('disconn').disabled="disabled";
	document.getElementById('send').disabled="disabled";*/
	if (ws != null) {
		ws.close();
		ws = null;
	}
	//setConnected(false);
}

function echo() {
	if(ws==null){
		alert('connection not established, please connect.');
		return;
	}
	var name = document.getElementById('username').value;
	if(isNull(name)){
		alert('请输入昵称!')
		return;
	}
	var group = document.getElementById('groupname').value;
	if(isNull(group)){
		alert('请输入群组!')
		return;
	}
	
	var destType=$("#destType option:selected").val();
	if(isNull(destType)){
		alert('请输入发送类型!')
		return;
	}
	var touser;
	if(destType==1){
		touser = document.getElementById('touser').value;
		if(isNull(touser)){
			alert('请输入目标成员!')
			return;
		}
	}else{
		touser = "";
	}
	var message = document.getElementById('msg').value;
	if(isNull(message)){
		log('请输入内容!')
		return;
	}
	var result = {
			username:name,
			group:group,
			destType:destType,
			touser:touser,
			content:message
	}
	var json = JSON.stringify(result);
	ws.send(json);
	document.getElementById('msg').value = '';
	document.getElementById('msg').focus();
};
function log(message) {
	var textObj = document.getElementById("view");
	var texta = textObj.value;
	//$("#view").html($("#view").html()+message);
	if(isNull(texta)){
		textObj.value=message;
	}else{
		textObj.value=texta+"\n"+message;
	}
}
/*function log(message) {
	var console = document.getElementById('view');
	var p = document.createElement('p');
	p.style.wordWrap = 'break-word';
	p.appendChild(document.createTextNode(message));
	console.appendChild(p);
	while (console.childNodes.length > 35) {
		console.removeChild(console.firstChild);
	}
	console.scrollTop = console.scrollHeight;
}*/