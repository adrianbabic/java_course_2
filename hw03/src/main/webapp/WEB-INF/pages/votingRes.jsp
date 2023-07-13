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
		<h1>Rezultati glasanja</h1>
 		<p>Ovo su rezultati glasanja.</p>
 		<table border="1" class="rez">
 			<thead><tr><th>Bend</th><th>Broj glasova</th></tr></thead>
 			<tbody>
 				<c:forEach var="one" items="${results}">
 					<tr><td>${one.name}</td><td>${one.votes}</td></tr>
				</c:forEach>
 			</tbody>
 		</table>

 		<h2>Grafički prikaz rezultata</h2>
		<img alt="Pie-chart" src="/webapp2/glasanje-grafika" />
		
		<h2>Rezultati u XLS formatu</h2>
		<p>Rezultati u XLS formatu dostupni su <a href="/webapp2/glasanje-xls">ovdje</a></p>
		
		<h2>Razno</h2>
		<p>Primjeri pjesama pobjedničkih bendova:</p>
		<ul>
			<c:forEach var="single" items="${winners}">
 				<li><a href="${single.song}" target="_blank">${single.name}</a></li>
			</c:forEach>
		</ul>
   	</body>
</html>