package pt.devhub.siu.portal;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.devhub.siu.service.ServiceProcessor;
import pt.devhub.siu.service.resolver.ServiceEntity;
import pt.devhub.siu.service.resolver.ServiceEntityType;

@Named
@ViewScoped
public class MainPortalController implements Serializable {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -437405400901656510L;

	// Injection of the NASA service processor
	@Inject
	@ServiceEntity(ServiceEntityType.NASA)
	private ServiceProcessor serviceProcessor;

	public MainPortalController() {
	}

	@PostConstruct
	public void init() {
	}

	public String executeAction(ServiceEntityType type) {
		serviceProcessor.processRequest();

		return "services/nasaService";
	}
}
