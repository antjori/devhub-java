package pt.devhub.siu.common.response.impl;

import lombok.Getter;
import pt.devhub.siu.common.response.IResponse;
import pt.devhub.siu.common.response.ext.google.ApiDiscovery;

/**
 * Object that contains the data that represents the Google API Discovery
 * response.
 */
public class GoogleResponse implements IResponse {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -1787321855957854846L;

	@Getter
	private ApiDiscovery apiDiscovery;

	/**
	 * Default constructor of this class.
	 *
	 * @param apiDiscovery
	 *            the API discovery
	 */
	public GoogleResponse(final ApiDiscovery apiDiscovery) {
		this.apiDiscovery = apiDiscovery;
	}

}
