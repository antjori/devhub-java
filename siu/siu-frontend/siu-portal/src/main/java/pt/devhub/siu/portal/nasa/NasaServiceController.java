package pt.devhub.siu.portal.nasa;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.devhub.siu.common.response.IResponse;
import pt.devhub.siu.common.response.impl.NasaResponse;
import pt.devhub.siu.service.ServiceProcessor;
import pt.devhub.siu.service.resolver.ServiceEntity;
import pt.devhub.siu.service.resolver.ServiceEntityType;

/**
 * The controller for the NASA page.
 */
@Named
@ViewScoped
public class NasaServiceController implements Serializable {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = 3046707241647696190L;

	private IResponse nasaResponse;

	// Injection of NASA's service processor
	@Inject
	@ServiceEntity(ServiceEntityType.NASA)
	private ServiceProcessor serviceProcessor;

	/**
	 * Default constructor for this class.
	 */
	public NasaServiceController() {
	}

	/**
	 * Executes the call to Nasa's APOD API.
	 */
	@PostConstruct
	public void init() {
		setNasaResponse(serviceProcessor.processRequest());
	}

	/**
	 * @param nasaResponse
	 *            the nasaResponse to set
	 */
	private void setNasaResponse(final IResponse nasaResponse) {
		this.nasaResponse = nasaResponse;
	}

	/*
	 * public void fetchNasaPodUrl() {
	 * System.out.println("fetchNasaPodUrl".toUpperCase());
	 * setPodUrl(serviceProcessor.processRequest()); }
	 */

	/**
	 * Returns the URL for the picture of the day.
	 * 
	 * @return the podUrl
	 */
	public String getPodUrl() {
		return getNasaResponse().getPodUrl();
	}

	/**
	 * Returns the title for the picture of the day.
	 * 
	 * @return the podTitle
	 */
	public String getPodTitle() {
		return getNasaResponse().getTitle();
	}

	/**
	 * Gets NASA's response making the proper cast.
	 * 
	 * @return NASA's response object
	 */
	private NasaResponse getNasaResponse() {
		return (NasaResponse) nasaResponse;
	}
}
