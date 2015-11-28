package pt.devhub.siu.portal;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.devhub.siu.service.ServiceProcessor;
import pt.devhub.siu.service.resolver.ServiceEntity;
import pt.devhub.siu.service.resolver.ServiceEntityType;

/**
 * The controller for the NASA page.
 */
@Named
@ViewScoped
public class NasaPortalController implements Serializable {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = 3046707241647696190L;

	private String podUrl;

	// Injection of the NASA service processor
	@Inject
	@ServiceEntity(ServiceEntityType.NASA)
	private ServiceProcessor serviceProcessor;

	/**
	 * Default constructor for this class.
	 */
	public NasaPortalController() {
	}

	@PostConstruct
	public void init() {
		setPodUrl(serviceProcessor.processRequest());
	}

	/*public void fetchNasaPodUrl() {
		System.out.println("fetchNasaPodUrl".toUpperCase());
		setPodUrl(serviceProcessor.processRequest());
	}*/

	/**
	 * Returns the URL for the picture of the day.
	 * 
	 * @return the podUrl
	 */
	public String getPodUrl() {
		return podUrl;
	}

	/**
	 * Sets the URL for the picture of the day.
	 * 
	 * @param podUrl
	 *            the podUrl to set
	 */
	public void setPodUrl(final String podUrl) {
		this.podUrl = podUrl;
	}
}
