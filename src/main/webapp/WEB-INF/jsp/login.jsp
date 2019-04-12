<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Login Page</title>
	</head>

	<body>
		<form action="webuser-authenticate" method="post">

			Please enter your user ID 		
			<input type="text" name="userId"/><br>		
			
			Please enter your password
			<input type="password" name="password"/>
			
			<input type="submit" value="LOGIN">			
		
		</form>
	</body>
</html>