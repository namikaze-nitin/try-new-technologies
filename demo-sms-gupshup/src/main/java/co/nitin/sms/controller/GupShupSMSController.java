package co.nitin.sms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.nitin.sms.config.UserPropsMapper;

public class GupShupSMSController {

	private Logger log =  LoggerFactory.getLogger(GupShupSMSController.class);
	private UserPropsMapper propsMapper;
	
	public GupShupSMSController(UserPropsMapper mapper) {
		this.propsMapper = mapper;
	}

	
}
