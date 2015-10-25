package pt.devhub.siu.core.bean;

import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.devhub.siu.core.bean.RequestManager;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

@Stateless
public class NasaRequestManager implements RequestManager {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -4885032369456830806L;

	private static final Logger logger = LoggerFactory.getLogger(NasaRequestManager.class);

	// NASA A.P.O.D. request
	private static final String APOD_REQUEST = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";

	@Override
	public void processRequest() {
		logger.info("Received a request and forwarding it to NASA");

		Resty resty = new Resty();
		JSONResource jsonResource = null;

		try {
			jsonResource = resty.json(APOD_REQUEST);
			if (jsonResource != null) {
				String url = (String) jsonResource.get("url");
				String mediaType = (String) jsonResource.get("media_type");
				String explanation = (String) jsonResource.get("explanation");
				logger.info(url);
				logger.info(mediaType);
				logger.info(explanation);
			}
		} catch (Exception e) {
			logger.error("An error occurred during request processing", e);
		}
	}

}
