package pt.devhub.siu.core.bean.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.devhub.siu.common.response.IResponse;
import pt.devhub.siu.common.response.ext.google.ApiDiscovery;
import pt.devhub.siu.common.response.ext.google.Item;
import pt.devhub.siu.common.response.impl.GoogleResponse;
import pt.devhub.siu.core.bean.api.RequestManager;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

/**
 * Bean responsible for handling the requests and dispatch them to the Google
 * API.
 */
@Stateless
public class GoogleRequestManager implements RequestManager {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -1426453921913912329L;

	// Google API Discovery request
	private static final String API_DISCOVERY_REQUEST = "https://www.googleapis.com/discovery/v1/apis";

	// The logger
	@Inject
	private Logger logger;

	@Override
	public IResponse processRequest() {
		logger.info("Received a request and forwarding it to Google's API Discovery service");

		Resty resty = new Resty();
		JSONResource jsonResource = null;
		IResponse response = null;

		try {
			ObjectMapper mapper = null;
			ApiDiscovery apiDiscovery = null;

			jsonResource = resty.json(API_DISCOVERY_REQUEST);

			if (jsonResource != null) {
				mapper = new ObjectMapper();
				apiDiscovery = mapper.readValue(jsonResource.toString(), ApiDiscovery.class);
			}

			response = new GoogleResponse(apiDiscovery);
		} catch (Exception e) {
			logger.error("An error occurred during request processing", e);
		}

		// print google's response

		for (Item item : ((GoogleResponse) response).getApiDiscovery().getItems()) {
			logger.debug(item.toString());
		}

		return response;
	}

}
