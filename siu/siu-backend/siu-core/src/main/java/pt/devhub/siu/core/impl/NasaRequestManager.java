package pt.devhub.siu.core.impl;

import javax.ejb.Stateless;

import pt.devhub.siu.core.RequestManager;

@Stateless(mappedName = "NasaRequestManager")
public class NasaRequestManager implements RequestManager {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = -4885032369456830806L;

	@Override
	public void processRequest() {
		// TODO Auto-generated method stub

	}

}
