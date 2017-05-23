package pt.devhub.siu.service.resolver;

import java.io.Serializable;

/**
 * Enumeration representative of the several services available on SIU.
 *
 */
public enum ServiceEntityType implements Serializable {

	NASA("services/nasa/apodService"), GOOGLE("services/google/apiDiscoveryService");

	private String url;

	private ServiceEntityType(final String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
