package pt.devhub.siu.core.bean.api;

import java.io.Serializable;

import javax.ejb.Remote;

import pt.devhub.siu.common.response.IResponse;

/**
 * Bean responsible for handling the request coming from the front-end.
 */
@Remote
public interface RequestManager extends Serializable {

	IResponse processRequest();
}
