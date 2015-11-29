package pt.devhub.siu.service.impl;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import pt.devhub.siu.common.entity.IResponse;
import pt.devhub.siu.core.bean.api.RequestManager;
import pt.devhub.siu.service.ServiceProcessor;

@Named
@RequestScoped
public class NasaServiceProcessor implements ServiceProcessor, Serializable {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -8803742285875290016L;

	// The logger
	@Inject
	private Logger logger;

	@EJB(lookup = "java:global/siu-core-ear/remote/NasaRequestManager!pt.devhub.siu.core.bean.api.RequestManager")
	private RequestManager requestManager;

	@Override
	public IResponse processRequest() {
		logger.info("Processing service request...");

		return requestManager.processRequest();
	}

}
