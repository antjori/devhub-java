package pt.devhub.siu.portal;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import pt.devhub.siu.service.resolver.ServiceEntityType;

/**
 * The main portal controller.
 * 
 * Interacts with SIU's main page.
 */
@Named
@ViewScoped
public class MainPortalController implements Serializable {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -437405400901656510L;

	// The logger
	@Inject
	private Logger logger;

	/**
	 * Default constructor for this class.
	 */
	public MainPortalController() {
	}

	/**
	 * Forwards the link request to the respective service entity page.
	 * 
	 * @param type
	 *            the service entity type
	 * @return the URL to be forwarded to
	 */
	public String executeAction(ServiceEntityType type) {
		logger.info("Forwarding to " + type.name() + " page...");

		return type.getUrl();
	}
}
