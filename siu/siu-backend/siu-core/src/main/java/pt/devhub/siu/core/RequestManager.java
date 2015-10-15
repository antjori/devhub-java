package pt.devhub.siu.core;

import java.io.Serializable;

import javax.ejb.Remote;

/**
 * Bean responsible for handling the request coming from the front-end.
 */
@Remote
public interface RequestManager extends Serializable {

	void processRequest();
}
