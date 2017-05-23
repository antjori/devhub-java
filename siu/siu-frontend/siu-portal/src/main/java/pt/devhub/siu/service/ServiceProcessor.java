package pt.devhub.siu.service;

import pt.devhub.siu.common.response.IResponse;

/**
 * The service processor interface, containing the signature of the method that
 * allows the process of the requests.
 */
@FunctionalInterface
public interface ServiceProcessor {

	public IResponse processRequest();
}
