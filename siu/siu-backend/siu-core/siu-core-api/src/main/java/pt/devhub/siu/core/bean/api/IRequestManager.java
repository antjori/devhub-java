package pt.devhub.siu.core.bean.api;

import java.io.Serializable;

import pt.devhub.siu.common.response.IResponse;

/**
 * Bean responsible for handling the requests coming from the front-end.
 */
public interface IRequestManager extends Serializable {

	IResponse processRequest();
}
