<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
 	<head>
    	<title>Homework 03</title>
    	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    	<style type="text/css"><jsp:include page="/WEB-INF/css/background.jsp" /></style>
  	</head>
   	<body>
   
 		<h1>Background color chooser</h1>   
		 	
 		<a href="/webapp2/setcolor?color=white">WHITE</a>
 		<a href="/webapp2/setcolor?color=red">RED</a>
 		<a href="/webapp2/setcolor?color=green">GREEN</a>
 		<a href="/webapp2/setcolor?color=cyan">CYAN</a>
 	
   	</body>
</html>