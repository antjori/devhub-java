package pt.devhub.siu.portal;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.devhub.siu.service.ServiceProcessor;
import pt.devhub.siu.service.resolver.ServiceEntity;
import pt.devhub.siu.service.resolver.ServiceEntityType;

@Named
@ViewScoped
public class MainPortalController {

	// Injection of the NASA service processor
	@Inject
	@ServiceEntity(ServiceEntityType.NASA)
	private ServiceProcessor serviceProcessor;
}
