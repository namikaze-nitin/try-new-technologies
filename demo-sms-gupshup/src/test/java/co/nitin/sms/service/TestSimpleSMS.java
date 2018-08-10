package co.nitin.sms.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class TestSimpleSMS {

	@Autowired UserPropsMapper mapper;
	static Logger log = LoggerFactory.getLogger("[TestSimpleSMS]");
	
	@Test
	public void numberFormatterTest() {
		
		log.info("[simpleSMSTest]");
		List<Long> number = new ArrayList<Long>();
		number.add(9870453104L);
		number.add(8209163590L);
		
		GupShupSMSService service = new GupShupSMSService(mapper);
		Assert.assertEquals(service.recepientNumberFormatter(number),"9870453104,8209163590");
	}
	
	@Test
	public void messageFormatterTest() throws UnsupportedEncodingException {
		
		log.info("[messageFormatterTest]");
		
		GupShupSMSService service = new GupShupSMSService(mapper);
		
		Assert.assertEquals( URLEncoder.encode(this.mapper.getPassword(), "UTF-8"), URLEncoder.encode("java@neurosys3", "UTF-8") );

		List<Long> number = new ArrayList<Long>();
		number.add(9870453104L);
		number.add(8209163590L);
		Assert.assertEquals(service.recepientNumberFormatter(number),"9870453104,8209163590");

		String[] var = {"1.1","json","TEXT","PLAIN"};
		String msgBody = service.messageBodyFormatter("Hello!!!", number, var);
//		System.out.println(service.messageBodyFormatter("Hello!!!", number, var));
		
		String data = "";
		data += "method=sendMessage";
		data += "&userid=2000181134"; // your loginId
		data += "&password=" + URLEncoder.encode("java@neurosys3", "UTF-8"); // your password
		data += "&msg=" + URLEncoder.encode("Hello!!!" , "UTF-8");
		data += "&send_to=" + URLEncoder.encode(service.recepientNumberFormatter(number), "UTF-8"); // a valid 10 digit phone no.
		data += "&v=1.1" ;
		data += "&format=json";
		data += "&msg_type=TEXT"; // Can by "FLASH" or "UNICODE_TEXT" or “BINARY”
		data += "&auth_scheme=PLAIN";
		
		Assert.assertEquals(msgBody, data);
	}
	
	@Test
	public void sendSimpleSMS() throws IOException, JSONException {
		
		GupShupSMSService service = new GupShupSMSService(mapper);
		
		List<Long> number = new ArrayList<Long>();
		number.add(9870453104L);
		number.add(8209163590L);
		
		String[] var = {"1.1","json","TEXT","PLAIN"};
		String response = service.sendSimpleSMS("Hello!!!", number, var);
		
		JSONObject resp = (JSONObject) new JSONObject(response).get("response");
		Assert.assertEquals(resp.get("status"), "success");
	}
}
