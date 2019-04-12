package com.adam.webapp;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

// Base path that all other paths extend from
@ApplicationPath("/webapp")
public class AppResourceConfig extends ResourceConfig {
	
	public AppResourceConfig() {
		packages("com.adam.webapp");
		property(JspMvcFeature.TEMPLATE_BASE_PATH, "/WEB-INF/jsp");
        register(JspMvcFeature.class);
	}

}