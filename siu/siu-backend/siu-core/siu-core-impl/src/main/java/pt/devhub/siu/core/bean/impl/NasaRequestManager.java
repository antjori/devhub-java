package pt.devhub.siu.core.bean.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import pt.devhub.siu.common.response.api.IResponse;
import pt.devhub.siu.common.response.impl.NasaResponse.NasaResponseBuilder;
import pt.devhub.siu.core.bean.api.RequestManager;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

/**
 * Bean responsible for handling the requests and dispatch them to the NASA API.
 */
@Stateless
public class NasaRequestManager implements RequestManager {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -4885032369456830806L;

	// NASA APOD request
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
	public IResponse processRequest() {
		logger.info("Received a request and forwarding it to NASA's APOD service");

		Resty resty = new Resty();
		JSONResource jsonResource = null;
		String url = StringUtils.EMPTY;
		NasaResponseBuilder responseBuilder = null;

		try {
			jsonResource = resty.json(APOD_REQUEST);

			if (jsonResource != null) {
				url = (String) jsonResource.get("url");

				logger.info("NASA's picture of the day URL: " + url);

				responseBuilder = new NasaResponseBuilder(url);
				responseBuilder.setTitle((String) jsonResource.get("title"));
				responseBuilder.setMediaType((String) jsonResource.get("media_type"));
				responseBuilder.setExplanation((String) jsonResource.get("explanation"));
			}
		} catch (Exception e) {
			logger.error("An error occurred during request processing", e);
		}

		return responseBuilder.build();
	}

}
