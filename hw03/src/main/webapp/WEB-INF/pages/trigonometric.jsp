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
 		<p>Here is the table of requested trigonometric functions:</p>

		<table border="1">
			<tr><th>Angle</th><th>Sin(x)</th><th>Cos(x)</th></tr>
			<c:forEach var="item" items="${results}">
  				<tr><td>${item.angle}</td><td>${item.sinValue}</td><td>${item.cosValue}</td></tr>
			</c:forEach>
		</table>
   	</body>
</html>