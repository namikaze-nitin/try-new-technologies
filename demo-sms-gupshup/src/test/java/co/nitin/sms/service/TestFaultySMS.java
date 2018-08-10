package co.nitin.sms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import co.nitin.sms.config.UserPropsMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestFaultySMS {

	@Autowired UserPropsMapper mapper;
	static Logger log = LoggerFactory.getLogger("[TestSimpleSMS]");
	
	@Test
	public void wrongNumberSizeTest() throws IOException, JSONException {
		
		log.info("[wrongSmsTest]");
		GupShupSMSService service = new GupShupSMSService(mapper);
		
		List<Long> phones = new ArrayList<Long>();
		phones.add(90000000000L);
		Assert.assertEquals("90000000000", service.recepientNumberFormatter(phones));
		
		String[] apiVar = {"1.1","json","TEXT","PLAIN"};
		String response = service.sendSimpleSMS("Hello darkness my old friend...", phones, apiVar);
		
		JSONObject resp = (JSONObject) new JSONObject(response).get("response");
		Assert.assertEquals("error", resp.get("status"));
		log.error("Error while sending sms : ",resp);
	}

	@Test
	public void nullNumberSizeTest() throws IOException, JSONException {
		
		log.info("[wrongSmsTest]");
		GupShupSMSService service = new GupShupSMSService(mapper);
		
		List<Long> phones = new ArrayList<Long>();
//		phones.add();
//		Assert.assertEquals("", service.recepientNumberFormatter(phones));
		
		String[] apiVar = {"1.1","json","TEXT","PLAIN"};
		String response = service.sendSimpleSMS("Hello darkness my old friend...", phones, apiVar);

		String resp = (String) new JSONObject(response).get("response");
		Assert.assertEquals("error", resp);
		log.error("Error while sending sms : ",resp);
	}

}
