package pt.devhub.siu.core.bean.impl;

import javax.ejb.Stateless;

import pt.devhub.siu.core.bean.RequestManager;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

@Stateless
public class NasaRequestManager implements RequestManager {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -4885032369456830806L;

	private static final String APOD_REQUEST = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";

	@Override
	public void processRequest() {
		System.out.println("@NasaRequestManager: Received a request and forwarding it to NASA...");
		Resty resty = new Resty();
		JSONResource jsonResource = null;
		try {
			jsonResource = resty.json(APOD_REQUEST);
			if (jsonResource != null) {
				String url = (String) jsonResource.get("url");
				String mediaType = (String) jsonResource.get("media_type");
				String explanation = (String) jsonResource.get("explanation");
				System.out.println(url + ";" + mediaType + ";" + explanation);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(jsonResource.toString());
	}

}
