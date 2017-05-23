package pt.devhub.siu.core.bean.timer;

import java.io.IOException;
import java.net.InetAddress;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;

@Startup
@Singleton
public class GooglePingTimerBean {

	@Inject
	private Logger logger;

	@Schedule(second = "*/10", minute = "*", hour = "*", persistent = false)
	public void atSchedule() {
		logger.info("Triggered scheduler for Google ping...");

		try {
			logger.info("Is Google alive? " + InetAddress.getByName("www.google.com").isReachable(2000));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		logger.info("Finished scheduler for Google ping!");
	}
}
