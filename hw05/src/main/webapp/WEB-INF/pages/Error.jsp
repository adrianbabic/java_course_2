<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Error</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<c:choose>
		<c:when test="${sessionScope['current.user.fn'] != null}">
			<div style="display: flex; align-items: center;">
				<p style="margin-right: 30px;">Logged as ${sessionScope['current.user.fn']}
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
	
	<c:if test="${message != null}">
		<p>${message}</p>
	</c:if>
	<c:if test="${message == null}">
		<p>Unknown error occured</p>
	</c:if>

	<br>

	<a href="/blog/servleti/authors">To authors list</a>
</body>
</html>