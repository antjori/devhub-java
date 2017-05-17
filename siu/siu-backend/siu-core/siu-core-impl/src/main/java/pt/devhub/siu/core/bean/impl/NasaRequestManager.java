package pt.devhub.siu.core.bean.impl;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;

import pt.devhub.siu.common.response.IResponse;
import pt.devhub.siu.common.response.impl.NasaResponse.NasaResponseBuilder;
import pt.devhub.siu.core.bean.api.IRequestManager;

/**
 * Bean responsible for handling the requests and dispatch them to the NASA API.
 */
@Stateless
@Remote(IRequestManager.class)
public class NasaRequestManager extends RequestManager {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -4885032369456830806L;

	// NASA APOD request
	private static final String APOD_REQUEST = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";

	/**
	 * Default constructor of the class.
	 */
	public NasaRequestManager() {
	}

	@Override
	public IResponse processRequest() {
		logger.info("Received a request and forwarding it to NASA's APOD service");

		IResponse response = null;

		try {
			String url = StringUtils.EMPTY;
			String title = StringUtils.EMPTY;
			String mediaType = StringUtils.EMPTY;
			String explanation = StringUtils.EMPTY;

			executeRestCall(APOD_REQUEST);

			if (restCallSuccessful()) {
				url = getParameter("url");

				logger.info("NASA's picture of the day URL: " + url);

				title = getParameter("title");
				mediaType = getParameter("media_type");
				explanation = getParameter("explanation");
			}

			NasaResponseBuilder responseBuilder = new NasaResponseBuilder(url);
			responseBuilder.setTitle(title);
			responseBuilder.setMediaType(mediaType);
			responseBuilder.setExplanation(explanation);

			response = responseBuilder.build();
		} catch (Exception e) {
			logger.error("An error occurred during request processing", e);
		} finally {
			invalidateRestCall();
		}

		return response;
	}

}
