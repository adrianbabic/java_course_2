<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Hw05 - Blog Entry</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<c:choose>
		<c:when test="${sessionScope['current.user.fn'] != null}">
			<div style="display: flex; align-items: center;">
				<p style="margin-right: 30px;">Logged as
					${sessionScope['current.user.fn']}
					${sessionScope['current.user.ln']}</p>
				<a href="/blog/servleti/logout">Logout</a>
			</div>
		</c:when>
		<c:otherwise>
			<div style="display: flex; align-items: center;">
				<p style="margin-right: 30px;">Not logged in</p>
				<a href="/blog/servleti/main">Login</a>
			</div>
		</c:otherwise>
	</c:choose>

	<h1>Create new blog entry</h1>

	<c:if test="${message != null}">
		<p>${message}</p>
	</c:if>

	<c:choose>
		<c:when test="${sessionScope['current.user.nick'] != null}">
			<form method="post" action="/blog/servleti/author/${sessionScope['current.user.nick']}/new">
				<label for="title">Title:</label> 
				<input type="text" id="title" name="title" required>
				<br> <br> 
				<div style="display: flex; align-items: center;">
					<label for="text">Text:</label>
					<textarea id="text" name="text" rows="6" required></textarea>
				</div>
				<br> <br>
				 <input type="hidden" id="nick" name="nick" value="${sessionScope['current.user.nick']}">
				<input type="submit" value="Create">
			</form>
		</c:when>
		<c:otherwise>
			<p>You have to login first to be able to create a blog entry.</p>
			<a href="/blog/servleti/authors">Go to the list of authors</a>
		</c:otherwise>
	</c:choose>

</body>
</html>