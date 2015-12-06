package pt.devhub.siu.core.bean.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import pt.devhub.siu.common.entity.IResponse;
import pt.devhub.siu.core.bean.api.RequestManager;

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

		// TODO Auto-generated method stub
		return null;
	}

}
