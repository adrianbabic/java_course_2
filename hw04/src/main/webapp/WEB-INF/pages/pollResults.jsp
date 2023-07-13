<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
 	<head>
    	<title>Homework 04</title>
    	<meta name="viewport" content="width=device-width, initial-scale=1.0">
  	</head>
   	<body>
		<h1>Rezulati glasanja</h1>
 		<p>Ovo su rezultati glasanja.</p>
 		<table border="1" class="rez">
 			<thead><tr><th>Naslov</th><th>Broj glasova</th></tr></thead>
 			<tbody>
 				<c:forEach var="one" items="${options}">
 					<tr><td>${one.title}</td><td>${one.votesCount}</td></tr>
				</c:forEach>
 			</tbody>
 		</table>

 		<h2>Grafički prikaz rezultata</h2>
		<img alt="Pie-chart" src="/voting-app/servleti/glasanje-grafika?pollID=${pollid}" />
		
		<h2>Rezultati u XLS formatu</h2>
		<p>Rezultati u XLS formatu dostupni su <a href="/voting-app/servleti/glasanje-xls?pollID=${pollid}">ovdje</a></p>
		
		<h2>Razno</h2>
		<p>Linkovi na pobjednike:</p>
		<ul>
			<c:forEach var="single" items="${winners}">
 				<li><a href="${single.link}" target="_blank">${single.title}</a></li>
			</c:forEach>
		</ul>
   	</body>
</html>