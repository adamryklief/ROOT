package com.adam.webapp.listener;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.adam.webapp.runnable.RandomNumberPush;

/*
 * Schedules PushService 
 */
@WebListener
public class JobSchedulerListener implements ServletContextListener {
	
	private ScheduledExecutorService scheduler;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		int secOfDayToStart;
        if(LocalDateTime.now().getMinute() >= 30)
            secOfDayToStart = LocalDateTime.now().plusHours(1)
                    .with(ChronoField.MINUTE_OF_HOUR, 30)
                    .with(ChronoField.SECOND_OF_MINUTE, 0)
                    .get(ChronoField.SECOND_OF_DAY);
        else
            secOfDayToStart = LocalDateTime.now()
                    .with(ChronoField.MINUTE_OF_HOUR, 30)
                    .with(ChronoField.SECOND_OF_MINUTE, 0)
                    .get(ChronoField.SECOND_OF_DAY);
        
        int initialDelay = secOfDayToStart - LocalDateTime.now().get(ChronoField.SECOND_OF_DAY);
		
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleWithFixedDelay(new RandomNumberPush(), initialDelay, 3600, TimeUnit.SECONDS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		scheduler.shutdownNow();
	}
}
