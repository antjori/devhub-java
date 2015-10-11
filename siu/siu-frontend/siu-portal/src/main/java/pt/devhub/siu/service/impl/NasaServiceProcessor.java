package pt.devhub.siu.service.impl;

import pt.devhub.siu.service.ServiceProcessor;

public class NasaServiceProcessor implements ServiceProcessor {

	@Override
	public void processRequest() {
		System.out.println("Processing service request...");	
	}

}
