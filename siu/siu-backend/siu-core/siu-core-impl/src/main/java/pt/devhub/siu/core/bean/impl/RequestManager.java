package pt.devhub.siu.core.bean.impl;

import java.io.IOException;

import javax.inject.Inject;

import org.slf4j.Logger;

import pt.devhub.siu.core.bean.api.IRequestManager;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

/**
 * Parent class for all request managers.
 */
public abstract class RequestManager implements IRequestManager {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = 1110802311886249590L;

	// The logger
	@Inject
	protected Logger logger;

	/**
	 * The JSON resource containing the API response.
	 */
	protected JSONResource jsonResource;

	/**
	 * Executes the REST service call.
	 *
	 * @param uri
	 *            the URI to get
	 * @throws IOException
	 *             if an error occurs while executing the REST call
	 */
	protected void executeRestCall(final String uri) throws IOException {
		logger.info("Calling URI " + uri);
		this.jsonResource = new Resty().json(uri);
		logger.info("Result of the call to URI " + uri + ": " + restCallSuccessful());
	}

	/**
	 * Verifies if the REST call was successful.
	 *
	 * @return the state of the REST call
	 */
	protected boolean restCallSuccessful() {
		return this.jsonResource != null;
	}

	/**
	 * Attains a parameter from the JSON object given a specific key.
	 *
	 * @param key
	 *            the parameter key
	 * @return the parameter associated with the specified key
	 * @throws Exception
	 *             if an error occurs while attaining the parameter associated
	 *             with the specified key
	 */
	protected String getParameter(final String key) throws Exception {
		return (String) this.jsonResource.get(key);
	}

	/**
	 * Invalidates the REST call parameters.
	 */
	protected void invalidateRestCall() {
		this.jsonResource = null;
	}

}
