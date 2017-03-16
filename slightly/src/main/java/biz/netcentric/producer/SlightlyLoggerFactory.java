package biz.netcentric.producer;

import java.io.Serializable;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * (CDI) Producer for {@link Logger}, using the FQCN (fully qualified class
 * name) as the logger name.
 * <p>
 * Usage: declare the logger with
 * <code>
 * @Inject private static Logger logger;
 * </code>
 * <br/>
 * instead of:
 * <br/>
 * <code>
 * Logger logger = LoggerFactory.getLogger(SomeClass.class);
 * </code>
 */
public class SlightlyLoggerFactory implements Serializable {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = 9018202658619656606L;

	/**
	 * Allows the creation of a new Slf4j logger instance.
	 * 
	 * @param injectionPoint
	 *            the injection point
	 * @return the Slf4j logger instance
	 */
	@Produces
	public Logger createLogger(final InjectionPoint injectionPoint) {
		Class<?> clazz = injectionPoint.getMember().getDeclaringClass();
		Logger logger = LoggerFactory.getLogger(clazz);

		return logger;
	}
}
