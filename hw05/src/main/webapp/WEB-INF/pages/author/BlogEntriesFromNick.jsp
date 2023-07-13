<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Hw05 - Blog entries</title>
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
    
    <a href="/blog/servleti/authors">Back to blog authors</a>
    <br>	
    	
	<c:if test="${sessionScope['current.user.nick'] eq author.nick}">
		<br>
        <a href="/blog/servleti/author/${author.nick}/new">Create new entry</a>
    </c:if>
	
	<h1>List of blog entries from ${author.nick}</h1>
	
	<br>
	
	<c:if test="${empty(blog_entries)}">
    	<p>This user hasn't created any blog entries yet.</p>
	</c:if>

	<ul>
		<c:forEach var="one" items="${blog_entries}">
			<li><a href="/blog/servleti/author/${author.nick}/${one.id}">${one.title}</a></li>
		</c:forEach>
	</ul>
</body>
</html>