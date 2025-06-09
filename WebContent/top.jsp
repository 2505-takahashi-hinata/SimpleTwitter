<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>簡易Twitter</title>
<!-- 12追記 -->
<link href="./css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="main-contents">
		<div class="header">
			<!-- 8追記 -->
			<c:if test="${ empty loginUser }">
				<a href="login">ログイン</a>
				<a href="signup">登録する</a>
			</c:if>
			<c:if test="${ not empty loginUser }">
				<a href="./">ホーム</a>
				<a href="setting">設定</a>
				<a href="logout">ログアウト</a>
			</c:if>
		</div>

		<!-- 仕様追加⑤つぶやきの絞り込み 日時・絞込ボタン表示 -->
		<form action="./" method="get">
			<!-- カレンダー表示 入力内容保持するためvalueで設定 -->
			日付： <input name="start" type="date" value="${start}">～<input name="end" type="date" value="${end}">
			<input type="submit" value="絞込">
		</form>

		<!-- 9追記 -->
		<c:if test="${ not empty loginUser }">
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
		</c:if>

		<!-- 10追記 -->
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
			<c:if test="${ isShowMessageForm }">
				<form action="message" method="post">
					いま、どうしてる？<br />
					<textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
					<br /> <input type="submit" value="つぶやく">（140文字まで）
				</form>
			</c:if>
		</div>

		<!-- 11追記 -->
		<div class="messages">
			<c:forEach items="${messages}" var="message">
				<div class="message">
					<div class="account-name">
						<!-- 実践問題②  アカウント名にリンクを設定-->
						<span class="account">
							<a href="./?user_id=<c:out value="${message.userId}"/> ">
								<c:out value="${message.account}" />
							</a>
						</span> <span class="name"><c:out value="${message.name}" /></span>
					</div>
					<div class="text">
						<!-- pre要素でつぶやき内の改行を反映させる -->
						<pre><c:out value="${message.text}" /></pre>
					</div>
					<div class="date">
						<fmt:formatDate value="${message.createdDate}"
							pattern="yyyy/MM/dd HH:mm:ss" />
					</div>
				</div>

				<!-- 仕様追加①つぶやき削除 -->
				<div class="deleteMessage">
					<!-- 条件：ログインしている場合、削除ボタン表示 -->
					<c:if test="${ loginUser.id == message.userId }">
						<form action="deleteMessage" method="post">
							<!-- message.idをhiddenを使い、message.idをdeleteMessageSerletに送る-->
							<input name="id" value="${message.id}" id="id" type="hidden">
							<input type="submit" value="削除">
						</form>
					</c:if>
				</div>

				<!-- 仕様追加②つぶやき編集 -->
				<div class="edit">
					<!-- 条件：ログインしている場合、編集ボタン表示 -->
					<c:if test="${ loginUser.id == message.userId }">
						<form action="edit" method="get">
							<!-- message.idをhiddenを使い、message.idをeditSerletに送る-->
							<input name="id" value="${message.id}" id="id" type="hidden">
							<input type="submit" value="編集">
						</form>
					</c:if>
				</div>
				<!-- 仕様追加③-1 つぶやきの返信欄表示 -->
				<div class="comments">
					<c:if test="${ isShowMessageForm }">
						<form action="comment" method="post">
							<br /><textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
							<br />
							<input name="messageId" value="${message.id}" id="id" type="hidden">
							<input type="submit" value="返信">（140文字まで）
						</form>
					</c:if>
					<!-- 仕様追加③-2 つぶやきの返信を表示 (返信コメントにはリンク,削除,編集機能なし)-->
					<c:forEach items="${comments}" var="comment">
						<div class="comment">
							<c:if test="${ comment.messageId == message.id }">
								<div class="account-name">
									<span class="account"> <c:out value="${comment.account}" /></span>
									<span class="name"><c:out value="${comment.name}" /></span>
								</div>
								<div class="text">
									<!-- pre要素でつぶやき内の改行を反映させる -->
									<pre><c:out value="${comment.text}" /></pre>
								</div>
								<div class="date">
									<fmt:formatDate value="${comment.createdDate}"
										pattern="yyyy/MM/dd HH:mm:ss" />
								</div>
							</c:if>
						</div>
					</c:forEach>
				</div>
			</c:forEach>
		</div>

		<div class="copyright">Copyright(c)takahashi</div>
	</div>
</body>
</html>
