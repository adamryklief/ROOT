package com.adam.webapp.entrypoint;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;

import com.adam.webapp.auth.Secured;
import com.adam.webapp.model.AttendancePayload;
import com.adam.webapp.model.Student;
import com.adam.webapp.service.AttendanceService;
import com.adam.webapp.service.AuthService;
import com.adam.webapp.service.WebUserAuthService;
import com.adam.webapp.service.WebUserStudentRecordService;

@Path("")
public class AppEntryPoints {	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
	@Path("/basicauth")
	public Response loginUser(Student student) {
		Response response = new AuthService().authenticateUsingBasic(student);
		return response;
	}
	
	@Secured
	@POST
	@Path("/student-logout")
	public Response logStudentOut(@Context ContainerRequestContext containerRequestContext) {
		Response response = new AuthService().logOutStudent(containerRequestContext);
		return response;
	}
	
	@Secured
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/attendance")
	public Response logAttendance(@Context ContainerRequestContext containerRequestContext,
			AttendancePayload payload) {
		Response response = new AttendanceService()
				.processLogAttendanceRequest(containerRequestContext, payload);
		return response;
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/webuser-login")
	public Viewable webuserLogin() {
		return new Viewable("/login");
	}	
	
	@POST
	@Consumes("application/x-www-form-urlencoded;charset=ISO-8859-1")
	@Produces(MediaType.TEXT_HTML)
	@Path("/webuser-authenticate")
	public Viewable verifyWebUser(@Context HttpServletRequest request,
			@FormParam("userId") String userId,
			@FormParam("password") String password) {
		return new WebUserAuthService().authenticateWebuser(request, userId, password);
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded;charset=ISO-8859-1")
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/webuser-attendance-record-request")
	public Response processWebUserAttendanceRecordRequest(@Context HttpServletRequest request,
			@FormParam("studentId") String studentId) {
		return new WebUserStudentRecordService().getStudentAttendanceRecords(request, studentId);
	}

}
