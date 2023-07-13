<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
 	<head>
    	<title>Homework 04</title>
    	<meta name="viewport" content="width=device-width, initial-scale=1.0">
  	</head>
   	<body>   
		
 		<h1>${poll.title}</h1>
 		
 		<p>${poll.message}</p>
 		
 		<ol>
 			<c:forEach var="one" items="${options}">
 				<li><a href="glasanje-glasaj?id=${one.id}">${one.title}</a></li>
			</c:forEach>
 		</ol>
		
   	</body>
</html>