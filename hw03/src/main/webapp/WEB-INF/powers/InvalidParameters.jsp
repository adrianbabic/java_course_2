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
 		<h1>Invalid parameters for /powers action</h1>   
		 	
 		<p>You entered invalid parameters while trying to dynamically create Excel document with powers of numbers.</p>
 		<p>You must enter values for all parameters: a, b and n.</p>
 		<p>Parameters a and b should be in range of [-100, 100]. Parameter n should be in range of [1, 5].</p>
   	</body>
</html>