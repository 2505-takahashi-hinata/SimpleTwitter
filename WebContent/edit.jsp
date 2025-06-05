<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>簡易Twitter</title>
<link href="./css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="main-contents">
		<div class="header">
			<a href="./">ホーム</a> <a href="setting">設定</a> <a href="logout">ログアウト</a>
		</div>

		<div class="profile">
			<div class="name">
				<h2>
					<c:out value="${loginUser.name}" />
				</h2>
			</div>
			<div class="account">
				@
				<c:out value="${loginUser.account}" />
			</div>
			<div class="description">
				<c:out value="${loginUser.description}" />
			</div>
		</div>

		<c:if test="${ not empty errorMessages }">
			<div class="errorMessages">
				<ul>
					<c:forEach items="${errorMessages}" var="errorMessage">
						<li><c:out value="${errorMessage}" />
					</c:forEach>
				</ul>
			</div>
			<c:remove var="errorMessages" scope="session" />
		</c:if>

		<div class="form-area">
			<form action="edit" method="post">
				つぶやき<br />
				<textarea name="text" cols="100" rows="5" class="tweet-box"><c:out
						value="${message.text}" /></textarea>
				<br /> <input name="id" value="${message.id}" id="id" type="hidden">
				<input type="submit" value="更新"><br />
			</form>
		</div>
		<a href="./">戻る</a>

		<div class="copyright">Copyright(c)takahashi</div>
	</div>
</body>
</html>