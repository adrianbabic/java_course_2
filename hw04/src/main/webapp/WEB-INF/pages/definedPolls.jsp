<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<title>Homework 04</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

	<h1>List of available polls:</h1>
	

	<c:if test="${empty(polls)}">
    	The polls list is empty.
	</c:if>

	<ul>
		<c:forEach var="one" items="${polls}">
			<li><a href="glasanje?pollID=${one.id}">${one.title}</a></li>
		</c:forEach>
	</ul>

</body>
</html>