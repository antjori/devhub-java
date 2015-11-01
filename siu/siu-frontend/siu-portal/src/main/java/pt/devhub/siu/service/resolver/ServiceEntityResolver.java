package pt.devhub.siu.service.resolver;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

import pt.devhub.siu.service.ServiceProcessor;
import pt.devhub.siu.service.impl.NasaServiceProcessor;

@SessionScoped
public class ServiceEntityResolver implements Serializable {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = 4005980494975945130L;

	@Produces
	@ServiceEntity(ServiceEntityType.NASA)
	public ServiceProcessor getNasaServiceProcessor(NasaServiceProcessor nasaServiceProcessor) {
		return nasaServiceProcessor;
	}

}
