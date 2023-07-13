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
 		<h1>Homework 03</h1>   
		 	
 		<a href="/webapp2/color-chooser">Background color chooser</a>
 		<br><br><br>
 		
 		<a href="/webapp2/trigonometric?a=0&b=90">Get first 90 trigonometric values</a>

 		<form action="trigonometric" method="GET">
 			Beginning angle:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
 			Ending angle:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
 			<input type="submit" value="Get table"><input type="reset" value="Reset">
		</form>
		
		<br><br><br>
		<a href="/webapp2/stories/funny.jsp">Funny story</a>
		
		<br><br><br>
		<a href="/webapp2/report.jsp">Show me OS usage</a>
		
		<br><br><br>
		<a href="/webapp2/powers?a=1&b=100&n=3">Generate Excel document for the first 100 numbers, up to the power of 3</a>
		
		<form action="powers" method="GET">
 			Starting number:<br><input type="number" name="a" min="-100" max="100" step="1" value="1"><br>
 			Ending number:<br><input type="number" name="b" min="-100" max="100" step="1" value="100"><br>
 			To the power of:<br><input type="number" name="n" min="1" max="5" step="1" value="3"><br>
 			<input type="submit" value="Get xls file"><input type="reset" value="Reset">
		</form>
		
		<br><br><br>
		<a href="/webapp2/appinfo.jsp">Check web app's running duration</a>
		
		<br><br><br>
		<a href="/webapp2/glasanje">Take me to voting</a>
		
   	</body>
</html>