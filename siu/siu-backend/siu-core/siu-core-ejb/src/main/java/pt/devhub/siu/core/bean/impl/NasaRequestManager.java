package pt.devhub.siu.core.bean.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import pt.devhub.siu.core.bean.RequestManager;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

@Stateless
public class NasaRequestManager implements RequestManager {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -4885032369456830806L;

	// NASA A.P.O.D. request
	private static final String APOD_REQUEST = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";

	// The logger
	@Inject
	private Logger logger;

	/**
	 * Default constructor of the class.
	 */
	public NasaRequestManager() {
	}

	@Override
	public String processRequest() {
		logger.info("Received a request and forwarding it to NASA");

		Resty resty = new Resty();
		JSONResource jsonResource = null;
		String url = StringUtils.EMPTY;

		try {
			jsonResource = resty.json(APOD_REQUEST);
			if (jsonResource != null) {
				url = (String) jsonResource.get("url");

				logger.info(url);
				logger.info((String) jsonResource.get("media_type"));
				logger.info((String) jsonResource.get("explanation"));
			}
		} catch (Exception e) {
			logger.error("An error occurred during request processing", e);
		}

		return url;
	}

}
