<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Hw 03 - subtask 3</title>
		<style type="text/css"><jsp:include page="/WEB-INF/css/background.jsp" /></style>
	</head>
	<body>
		<h1 style="color: <%= generateRandomColor() %>">What is it called when time ends up in jail?    Time behind bars! </h1>
		<%!
		    java.util.Random random = new java.util.Random();
	        
	        String generateRandomColor() {
	            int red = random.nextInt(256);
	            int green = random.nextInt(256);
	            int blue = random.nextInt(256);
	            String colorValue = "rgb(" + red + ", " + green + ", " + blue + ")";
	            return colorValue;
	        }
		%>
	</body>
</html>

