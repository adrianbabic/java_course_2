<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="javax.servlet.http.HttpSession" %>

<!DOCTYPE html>
<html>
<head>
<title>Homework 05 - Authors</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>	
	<c:choose>
        <c:when test="${sessionScope['current.user.fn'] != null}">
        	<div style="display: flex; align-items: center;">
				<p style="margin-right: 30px;">Logged as ${sessionScope['current.user.fn']} ${sessionScope['current.user.ln']}</p>
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
	
	<c:if test="${sessionScope['current.user.nick'] != null}">
		<a href="/blog/servleti/author/${sessionScope['current.user.nick']}/new">Create a blog</a>
   	</c:if>
	
	<h1>List of blog authors</h1>
	

	<c:if test="${empty(authors)}">
    	<p>Not one author has created a blog entry yet.</p>
	</c:if>

	<ul>
		<c:forEach var="one" items="${authors}">
			<li><a href="/blog/servleti/author/${one.nick}">${one.nick}</a></li>
		</c:forEach>
	</ul>

</body>
</html>