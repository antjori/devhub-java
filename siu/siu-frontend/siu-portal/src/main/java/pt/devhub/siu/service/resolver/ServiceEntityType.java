package pt.devhub.siu.service.resolver;

import java.io.Serializable;

public enum ServiceEntityType implements Serializable {

	NASA("services/nasaService");

	private String url;

	private ServiceEntityType(final String url) {
		this.setUrl(url);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
