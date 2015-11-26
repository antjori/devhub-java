package pt.devhub.siu.core.bean.api;

import java.io.Serializable;

import javax.ejb.Remote;

/**
 * Bean responsible for handling the request coming from the front-end.
 */
@Remote
//@Local
public interface RequestManager extends Serializable {

	String processRequest();
}
