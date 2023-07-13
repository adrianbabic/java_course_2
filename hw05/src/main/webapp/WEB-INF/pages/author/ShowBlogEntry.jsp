<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
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
	
	
	<c:choose>
		<c:when test="${blog_entry == null}">
		  There is no blog entry under this id: ${entry_id}
		</c:when>
		<c:otherwise>
			<br>
			<a href="/blog/servleti/author/${blog_entry.creator.nick}">Back to all blogs</a>
			<br>
			<h4>Author: ${blog_entry.creator.email}</h4>
			<h1><c:out value="${blog_entry.title}"/></h1>
			<p><c:out value="${blog_entry.text}"/></p>
			<c:if test="${sessionScope['current.user.nick'] != null}">
				<c:if test="${sessionScope['current.user.nick'] eq blog_entry.creator.nick}">
					<form method="get" action="/blog/servleti/author/${blog_entry.creator.nick}/edit">
						<input type="hidden" id="entry_id" name="entry_id" value="${blog_entry.id}">
						<input type="submit" value="Edit">
					</form>
				</c:if>
			</c:if>
			<c:if test="${!blog_entry.comments.isEmpty()}">
				<br>
				<p>----------------------------------------------------</p>
				<h4>Comments:</h4>
				<ul>
				<c:forEach var="e" items="${blog_entry.comments}">
					<li>
						<div style="font-weight: bold">[User=<c:out value="${e.usersEMail}"/>] <c:out value="${e.postedOn}"/></div>
						<div style="padding-left: 10px;"><c:out value="${e.message}"/></div>
					</li>
				</c:forEach>
				</ul>
			</c:if>
			
			<c:if test="${sessionScope['current.user.nick'] != null}">
				<h4>Add comment</h4>
				<form method="post" action="/blog/servleti/author/${blog_entry.creator.nick}/${blog_entry.id}">
					<div style="display: flex; align-items: center; ">
						<label for="message">Message:</label>
						<textarea id="message" name="message" rows="6" required></textarea>
					</div>
					<br>
					<input type="hidden" id="commentators_nick" name="commentators_nick" value="${sessionScope['current.user.nick']}">
					<input type="submit" value="Add">
				</form>
			</c:if>
		</c:otherwise>
	</c:choose>
	
	<br>

  </body>
</html>