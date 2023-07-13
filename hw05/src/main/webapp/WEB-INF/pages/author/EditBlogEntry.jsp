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
		  You should provide blog_entry to render this page.
		</c:when>
		<c:otherwise>			
			<c:if test="${sessionScope['current.user.nick'] != null}">
				<c:if test="${sessionScope['current.user.nick'] eq blog_entry.creator.nick}">
					<form method="post" action="/blog/servleti/author/${blog_entry.creator.nick}/edit">
						<label for="title">Title:</label> 
						<input type="text" id="title" name="title" required value="${blog_entry.title}">
						<br><br> 
						<div style="display: flex; align-items: center; ">
							<label for="message">Text:</label>
							<textarea id="text" name="text" rows="6" required>${blog_entry.text}</textarea>
						</div>
						<input type="hidden" id="entry_id" name="entry_id" value="${blog_entry.id}">
						<br><br> 
						<input type="submit" value="Save changes">
					</form>
				</c:if>
			</c:if>
		</c:otherwise>
	</c:choose>
	
	<br>

  </body>
</html>