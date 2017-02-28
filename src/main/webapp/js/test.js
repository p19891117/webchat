$(function() {
	//setInterval("getSum()", 10000);
	var client = new SocketClient("http://localhost:8080//nsrep-webchat/sockjs/echo","zhangsan",[1,2,3],
		function(){
			$('#msg').focus();
				$('#msg').attr("disabled",false);
				$('#conn').attr("disabled","disabled");
				$('#disconn').attr("disabled",false);
				$('#send').attr("disabled",false);
				$('#destType').attr("disabled",false);
				$('#groupname').attr("disabled","disabled");
				client.viewMsg("连接服务器成功");
			},
		function(e){
			$('#msg').attr("disabled","disabled");
			$('#conn').attr("disabled",false);
			$('#disconn').attr("disabled","disabled");
			$('#send').attr("disabled","disabled");
			$('#destType').attr("disabled","disabled");
			$('#groupname').attr("disabled",false);
			if (e.code == 1007) {
				client.viewMsg('小样，昵称太长了!');
			}
		},
		function(msg){
			client.viewMsg(msg);
		}
	);
	$("#destType").change(function(){
		if($(this).val() == "1"){
			$("#touser").attr("disabled",false);
		}else{
			$("#touser").attr("disabled","disabled");
		}
	});
	$("#conn").click(function(){
		client.conn();
	});
	$("#disconn").click(function(){
		client.disconn();
	});
	$("#send").click(function(){
		var groupname=$("#groupname option:selected").val();
		if(client.isNull(groupname)){
			alert('请输入群组!')
			return;
		}
		
		var destType=$("#destType option:selected").val();
		if(client.isNull(destType)){
			alert('请输入发送类型!')
			return;
		}
		var touser;
		if(destType==1){
			touser = $('#touser').val();
			if(client.isNull(touser)){
				alert('请输入目标成员!')
				return;
			}
		}else{
			touser = "";
		}
		var message = $('#msg').val();
		if(client.isNull(message)){
			log('请输入内容!')
			return;
		}
		var result = {
				group:groupname,
				destType:destType,
				touser:touser,
				content:message
		}
		client.send(result);
		$('#msg').val("");
		$('#msg').focus();
	});
})