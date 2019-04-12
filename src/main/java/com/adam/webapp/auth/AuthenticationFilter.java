package com.adam.webapp.auth;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.adam.webapp.dao.MySqlAttendanceDao;
import com.adam.webapp.entity.StudentEntity;

import javax.ws.rs.Priorities;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	
	private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class.getName());
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    	
    	LOGGER.info("Received request @Secured...");

        String authorisationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Validate the Authorisation header
        if (authorisationHeader == null || !authorisationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ")) {
        	abortWithUnauthorised(requestContext);
            return;
        }

        // Extract the token from the Authorisation header
        String authToken = authorisationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
        
        MySqlAttendanceDao mySqlAttendanceDao = new MySqlAttendanceDao();
        StudentEntity studentEntity;
		try {
			mySqlAttendanceDao.openConnection();
			studentEntity =  mySqlAttendanceDao.getStudentByAuthToken(authToken);
		} finally {
			mySqlAttendanceDao.closeConnection();
		}
        
		// Verify token against database records
        if(studentEntity == null) {
        	LOGGER.info("Invalid token. Aborting...");
        	abortWithUnauthorised(requestContext);
        }
        LOGGER.info("Token is valid. Carrying out request...");
        requestContext.setProperty("student", studentEntity);
        
    }

    private void abortWithUnauthorised(ContainerRequestContext requestContext) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).build());
    }

}