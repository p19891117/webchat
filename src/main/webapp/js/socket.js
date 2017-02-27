$(function() {
	setInterval("getSum()", 10000);
})
// 目前是通过不停的轮询的方式获取在线人数的，还没想到更好的办法
function getSum() {
	$.ajax({
		url :"chat/getSum",
		type : 'get',
		success : function(data, status) { $("#sum").val("在线人数：" + data); },
		error : function(data, status, e) { }
	});
}
$("#destType").change(function(){
	if($(this).val() == "1"){
		$("#touser").attr("disabled",false);
	}else{
		$("#touser").attr("disabled","disabled");
	}
});
var ws = null;
var tmp = JSON.stringify({
		username:name,
		group:[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,25,267,28,29],
	});
tmp = BASE64.encoder(tmp);
tmp = tmp.replace("/","-");
var transports = {
	sessionId:function(){
		return tmp;
	}
};
function connect(url) {
	var name = $('#username').val();
	if(isNull(name)){
		alert('请输入昵称!')
		return;
	}
	var groupname=$("#groupname option:selected").val();
	if(isNull(groupname)){
		alert('请输入群组!')
		return;
	}
	var json = JSON.stringify({
		username:name,
		group:groupname,
		create:true
		});
	if (!url) {
		alert('Select whether to use W3C WebSocket or SockJS');
		return;
	}
	log(url);
	ws = new SockJS(url, undefined, transports);
	ws.onopen = function() {
		$('#msg').focus();
		$('#msg').attr("disabled",false);
		$('#conn').attr("disabled","disabled");
		$('#disconn').attr("disabled",false);
		$('#send').attr("disabled",false);
		$('#destType').attr("disabled",false);
		$('#groupname').attr("disabled","disabled");
		// 通过发送一条带有特殊符号的信息告诉后台登陆者的昵称，很蛋疼
		// 最好是可以在创建连接时就将这个信息一并带回服务器的，但是我还没找到可以用的这样的方法
		ws.send(json);
		log('服务器连接成功!');
	};
	ws.onmessage = function(event) {
		log(event.data);
	};
	ws.onclose = function(event) {
		$('#msg').attr("disabled","disabled");
		$('#conn').attr("disabled",false);
		$('#disconn').attr("disabled","disabled");
		$('#send').attr("disabled","disabled");
		$('#destType').attr("disabled","disabled");
		$('#groupname').attr("disabled",false);
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
	if (ws != null) {
		ws.close();
		ws = null;
	}
}

function echo() {
	if(ws==null){
		alert('connection not established, please connect.');
		return;
	}
	var name = $('#username').val();
	if(isNull(name)){
		alert('请输入昵称!')
		return;
	}
	var groupname=$("#groupname option:selected").val();
	if(isNull(groupname)){
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
		touser = $('#touser').val();
		if(isNull(touser)){
			alert('请输入目标成员!')
			return;
		}
	}else{
		touser = "";
	}
	var message = $('#msg').val();
	if(isNull(message)){
		log('请输入内容!')
		return;
	}
	var result = {
			username:name,
			group:groupname,
			destType:destType,
			touser:touser,
			content:message
	}
	var json = JSON.stringify(result);
	ws.send(json);
	$('#msg').val("");
	$('#msg').focus();
};
function log(message) {
	if(isNull($("#view").val())){
		$("#view").val(message);
	}else{
		$("#view").val($("#view").val()+"\n"+message);
	}
}
