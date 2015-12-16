package pt.devhub.siu.service.impl;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import pt.devhub.siu.common.response.IResponse;
import pt.devhub.siu.core.bean.api.RequestManager;
import pt.devhub.siu.service.ServiceProcessor;

/**
 * Processor for all Google's service requests.
 */
@Named
@RequestScoped
public class GoogleServiceProcessor implements ServiceProcessor, Serializable {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -4971099008758292311L;

	// The logger
	@Inject
	private Logger logger;

	@EJB(lookup = "java:global/siu-core-ear/remote/GoogleRequestManager!pt.devhub.siu.core.bean.api.RequestManager")
	private RequestManager requestManager;

	@Override
	public IResponse processRequest() {
		logger.info("Processing service request to Google API...");

		return requestManager.processRequest();
	}

}
