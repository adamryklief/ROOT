package com.adam.webapp.runnable;

import java.util.logging.Logger;

import com.adam.webapp.service.PushService;

public class RandomNumberPush implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(RandomNumberPush.class.getName());

	@Override
	public void run() {
		LOGGER.info("Running scheduled service...");
		new PushService().pushRandomNumberToClients();
	}

}
