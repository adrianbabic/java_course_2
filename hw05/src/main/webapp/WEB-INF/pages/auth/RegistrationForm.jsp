<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Hw05 - Registration</title>
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

	<h1>Registration</h1>


	<a href="/blog/servleti/main">Login</a>
	<br>
	<br>

	<c:if test="${message != null}">
		<p>${message}</p>
	</c:if>
	
	<c:if test="${sessionScope['current.user.id'] == null}">
		<form method="post" action="/blog/servleti/register">
			<label for="firstName">First Name:</label> <input type="text"
				id="firstName" name="firstName" required value="${firstName}"><br>
			<br> <label for="lastName">Last Name:</label> <input type="text"
				id="lastName" name="lastName" required value="${lastName}"><br>
			<br> <label for="email">Email:</label> <input type="email"
				id="email" name="email" required value="${email}"><br>
			<br> <label for="nick">Nick:</label> <input type="text" id="nick"
				name="nick" required value="${nick}"><br>
			<br> <label for="password">Password:</label> <input
				type="password" id="password" name="password" required><br>
			<br> <input type="submit" value="Register">
		</form>
	</c:if>
</body>
</html>
