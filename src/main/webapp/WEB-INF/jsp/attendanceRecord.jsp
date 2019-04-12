<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="com.adam.webapp.entity.UserEntity"%>
<!DOCTYPE html>
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Login Page</title>
	</head>

	<body>
		<form action="webuser-attendance-record-request" method="post">

			Please enter a student id to view a student's records
			<input type="text" name="studentId"/><br>		
			
			<input type="submit" value="VIEW RECORD">			
		
		</form>
	</body>
</html>