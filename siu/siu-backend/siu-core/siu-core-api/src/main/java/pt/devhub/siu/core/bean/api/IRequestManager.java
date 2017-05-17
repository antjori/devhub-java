package pt.devhub.siu.core.bean.api;

import java.io.Serializable;

import pt.devhub.siu.common.response.IResponse;

/**
 * Bean responsible for handling the requests coming from the front-end.
 */
@FunctionalInterface
public interface IRequestManager extends Serializable {

	/**
	 * Declaration of the abstract method that executes the request processing.
	 */
	IResponse processRequest();
}
