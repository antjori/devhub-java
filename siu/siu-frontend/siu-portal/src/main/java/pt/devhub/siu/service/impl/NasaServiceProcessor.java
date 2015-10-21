package pt.devhub.siu.service.impl;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.devhub.siu.core.bean.RequestManager;
import pt.devhub.siu.service.ServiceProcessor;

@Named
@RequestScoped
public class NasaServiceProcessor implements ServiceProcessor, Serializable {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -8803742285875290016L;

	// Default logger
	private static final Logger logger = LoggerFactory.getLogger(NasaServiceProcessor.class);

	@EJB(beanName = "NasaRequestManager")
	private RequestManager requestManager;

	@Override
	public void processRequest() {
		logger.info("Processing service request...");
		requestManager.processRequest();
	}

}
