package pt.devhub.siu.core.bean.timer;

import java.io.IOException;
import java.net.InetAddress;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

import org.slf4j.Logger;

@Startup
@Singleton
public class NasaPingTimerBean {

	@Resource
	private TimerService timerService;

	@Inject
	private Logger logger;

	@PostConstruct
	private void init() {
		TimerConfig timerConfig = new TimerConfig();
		timerConfig.setInfo(this.getClass().getName());
		timerConfig.setPersistent(false);

		timerService.createIntervalTimer(10000, 10000, timerConfig);
	}

	@Timeout
	public void execute(final Timer timer) {
		logger.info("Triggered scheduler for Nasa ping...");
		try {
			logger.info("Is Nasa alive? " + InetAddress.getByName("www.nasa.gov").isReachable(1000));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		logger.info("Finished scheduler for Nasa ping!");
	}
}
