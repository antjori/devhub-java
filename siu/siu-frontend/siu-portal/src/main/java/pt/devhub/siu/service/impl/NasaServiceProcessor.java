package pt.devhub.siu.service.impl;

import javax.ejb.EJB;

import pt.devhub.siu.core.RequestManager;
import pt.devhub.siu.service.ServiceProcessor;
import pt.devhub.siu.service.resolver.ServiceEntity;
import pt.devhub.siu.service.resolver.ServiceEntity.ServiceEntityType;

@ServiceEntity(ServiceEntityType.NASA)
public class NasaServiceProcessor implements ServiceProcessor {

	@EJB(mappedName = "NasaRequestManager#pt.devhub.siu.core.RequestManager")
	private RequestManager requestManager;

	@Override
	public void processRequest() {
		System.out.println("Processing service request...");
		requestManager.processRequest();
	}

}
