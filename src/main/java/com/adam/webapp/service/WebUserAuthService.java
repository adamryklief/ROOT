package com.adam.webapp.service;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.glassfish.jersey.server.mvc.Viewable;

import com.adam.webapp.dao.MySqlWebuserDao;
import com.adam.webapp.entity.UserEntity;
import com.adam.webapp.util.AuthUtil;

public class WebUserAuthService {
	
	private static final Logger LOGGER = Logger.getLogger(WebUserAuthService.class.getName());
	
	public Viewable authenticateWebuser(HttpServletRequest request, String userId, String password ) {
		if(!userId.contentEquals("")) {
			MySqlWebuserDao mySqlWebuserDao = new MySqlWebuserDao();
			try {
				mySqlWebuserDao.openConnection();
				UserEntity userEntity = mySqlWebuserDao.getUserByUserId(userId);
				if(userEntity != null) {
					if(AuthUtil.isHashVerified(password, userEntity.getPasswordHash())) {
						HttpSession session = request.getSession(true);
						return new Viewable("/attendanceRecord");
					}
				}
			} catch(Exception e) {
				LOGGER.severe(e.getMessage());
			} finally {
				mySqlWebuserDao.closeConnection();
			}
		}
		return new Viewable("/invalidLogin");
	}

}
