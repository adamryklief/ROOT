package com.adam.webapp.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.adam.webapp.service.AttendanceService;
import com.adam.webapp.service.PushService;

/*
 * This class is used to initialise the ServletContext static fields
 * of other classes for them to be able to retrieve sensitive keys
 * stored on the server 
 */
@WebListener
public class ContextParamLoaderListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		PushService.setServletContext(servletContext);
		AttendanceService.setServletContext(servletContext);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {}

}
