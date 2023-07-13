<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
 	<head>
    	<title>Homework 03</title>
    	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    	<style type="text/css"><jsp:include page="/WEB-INF/css/background.jsp" /></style>
  	</head>
   	<body>   
		
 		<h1>Glasanje za omiljeni bend:</h1>
 		
 		<p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!</p>
 		
 		<ol>
 			<c:forEach var="one" items="${candidates}">
 				<li><a href="glasanje-glasaj?id=${one.id}">${one.name}</a></li>
			</c:forEach>
 		</ol>
		
   	</body>
</html>