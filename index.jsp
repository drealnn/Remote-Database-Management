<!DOCTYPE html">
<!-- WelcomeServlet.html -->
<html lang="en">
<head>
   <title>Basic Welcome Servlet</title>
   <style type="text/css">
    <!--
	    body{background-color: blue; color: white; text-align: center;}
	-->
	
	
		#tableHead
		{
			background-color: red;
		}
		
		.tableBody
		{
			background-color: black;
		}
		
		.successBox
		{
			background-color: green;
		}
	</style>
</head>
<body>
   <form action = "/Project4/MySqlServlet" method = "post" name = "sqlBox">
   <p>
		<textarea rows="10" cols="50" name="sqlquery" ></textarea> <br>
		<input type = "submit" value = "Execute Command" />
		<input type = "button" value = "clear"/>
		
       
	</p>
	</form>
	
	
	
	<br>
	<br>
	
	<%   
		String[][] table = (String[][])request.getAttribute("table");
		String rowsChanged = (String) request.getAttribute("rowsChanged");
		String statusMarks = (String) request.getAttribute("statusMarks");
		
	
		if (table == null && rowsChanged == null && statusMarks == null)
		{
			
		
	%>
		<p> The table is null </p>
	<%
		} else if (table != null){
	%>
	
		<table border=1 align=center >
		<tr id="tableHead">
	<%
			
		for (String n : table[0])
		{
	%>
	
			<td> <%=n%> </td>
	<%
		}
	%>
			</tr>
	<%
		for (int i = 1; i < table.length; i++)
		{
	%>
			<tr class="tableBody">
	<%
			for (String n : table[i])
			{
			
	%>
				<td> <%=n%> </td>
	<%		}
	%>
			</tr>
	<%
		}
	%>
		</table>
	<%
		} else if (rowsChanged != null && statusMarks != null)
		{
			
	%>
			<div class="successBox">
			<p> <h3>The statement executed successfully. <br>
					<%=rowsChanged%> number of rows affected.
					</h3>
			</p>
			<br>
			<p> <h3>Business logic updated <%=statusMarks%> supplier status marks. </h3> </p>
			</div>
	<%
		}
	%>
			
	
</body>
</html>