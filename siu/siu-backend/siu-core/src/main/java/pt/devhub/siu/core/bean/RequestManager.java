package pt.devhub.siu.core.bean;

import java.io.Serializable;

import javax.ejb.Local;

/**
 * Bean responsible for handling the request coming from the front-end.
 */
@Local
public interface RequestManager extends Serializable {

	void processRequest();
}
