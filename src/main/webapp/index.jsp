<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<link rel="stylesheet" href="css/index.css">
<link rel="stylesheet" href="css/bootstrap.css">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<title>聊天室</title>
</head>
<body>
	<noscript> <h2 style="color: #ff0000">此应用需要开启javascript</h2> </noscript>
	<div class="container-fluid">
		<div class="row">
		  <div class="col-md-2">
				<input class="form-control" type="text" placeholder="用户名" id="username"><br>
				<!-- <input class="form-control" type="text" placeholder="组名" id="groupname"><br> -->
				<select class="form-control" id="groupname" >
					<option value="1">测试组1</option>
					<option value="2">测试组2</option>
					<option value="3">测试组3</option>
				</select><br>
				<select class="form-control" id="destType" disabled="disabled">
					<option value="0">组</option>
					<option value="1">用户</option>
				</select><br>
				<input class="form-control" type="text" placeholder="目标成员" id="touser" disabled="disabled">
				<button type="button" class="btn btn-primary" id="conn" onclick="connect('${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}/${pageContext.request.contextPath}/sockjs/echo');">上线</button>
				<button type="button" class="btn btn-primary" id="disconn" onclick="disconnect();" disabled="disabled">下线</button>
				<input class="form-control" type="text" id="sum" disabled="disabled">
			</div>
		  <div class="col-md-10"><textarea class="form-control" rows="20" id="view" style="resize: none;"  readonly="readonly"></textarea></div>
		</div>
		<div class="row">
		  <div class="col-md-12"><textarea class="form-control" rows="4" id="msg" style="resize: none;" disabled="disabled"></textarea></div>
		</div>
		<div class="row">
		  <div class="col-md-12"><button type="button"  class="btn btn-primary" disabled="disabled" id="send" onclick="echo();">发送</button></div>
		</div>
	</div>
</body>
<script src="js/jquery-3.1.1.js"></script>
<script src="js/sockjs.js"></script>
<script type="text/javascript" src="js/socket.js"></script>
</html>