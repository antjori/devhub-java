package pt.devhub.siu.service.impl;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

import pt.devhub.siu.core.RequestManager;
import pt.devhub.siu.service.ServiceProcessor;

@Named
@RequestScoped
public class NasaServiceProcessor implements ServiceProcessor, Serializable {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -8803742285875290016L;

	@EJB(beanName = "NasaRequestManager")
	private RequestManager requestManager;

	@Override
	public void processRequest() {
		System.out.println("@NasaServiceProcessor: Processing service request...");
		requestManager.processRequest();
	}

}
