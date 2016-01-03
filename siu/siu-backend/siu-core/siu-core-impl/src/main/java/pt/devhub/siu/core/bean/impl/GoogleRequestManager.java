package pt.devhub.siu.core.bean.impl;

import javax.ejb.Stateless;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.devhub.siu.common.response.IResponse;
import pt.devhub.siu.common.response.ext.google.ApiDiscovery;
import pt.devhub.siu.common.response.ext.google.Item;
import pt.devhub.siu.common.response.impl.GoogleResponse;

/**
 * Bean responsible for handling the requests and dispatch them to the Google
 * API.
 */
@Stateless
public class GoogleRequestManager extends RequestManager {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -1426453921913912329L;

	// Google API Discovery request
	private static final String API_DISCOVERY_REQUEST = "https://www.googleapis.com/discovery/v1/apis";

	@Override
	public IResponse processRequest() {
		logger.info("Received a request and forwarding it to Google's API Discovery service");

		IResponse response = null;

		try {
			ObjectMapper mapper = null;
			ApiDiscovery apiDiscovery = null;

			executeRestCall(API_DISCOVERY_REQUEST);

			if (restCallSuccessful()) {
				mapper = new ObjectMapper();
				apiDiscovery = mapper.readValue(jsonResource.toString(), ApiDiscovery.class);
			}

			response = new GoogleResponse(apiDiscovery);
		} catch (Exception e) {
			logger.error("An error occurred during request processing", e);
		} finally {
			invalidateRestCall();
		}

		// print google's response

		for (Item item : ((GoogleResponse) response).getApiDiscovery().getItems()) {
			logger.debug(item.toString());
		}

		return response;
	}

}
