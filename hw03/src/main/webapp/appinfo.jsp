<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.concurrent.TimeUnit" %>

<%
    ServletContext servletContext = request.getServletContext();
    long startTime = (long) servletContext.getAttribute("startTime");
    long duration = System.currentTimeMillis() - startTime;
    long days = TimeUnit.MILLISECONDS.toDays(duration);
    long hours = TimeUnit.MILLISECONDS.toHours(duration) % 24;
    long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
    long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
    long milliseconds = duration % 1000;
    String formattedDuration = String.format("%d days %d hours %d minutes %d seconds %d milliseconds",
        days, hours, minutes, seconds, milliseconds);
%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Homework 03</title>
		<style type="text/css"><jsp:include page="/WEB-INF/css/background.jsp" /></style>
	</head>
	<body>
		<h1>Subtask 06</h1>   
		<p>The web app has been running for <%= formattedDuration %>.</p>
	</body>
</html>
