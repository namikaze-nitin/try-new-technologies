package co.nitin.sms.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.nitin.sms.config.UserPropsMapper;

/**
 * Service class handling single or multiple recipient for a single message.
 * 
 * @author Nitin Sharma
 *
 */
@Service
public class GupShupSMSService {

	private Logger log =  LoggerFactory.getLogger(GupShupSMSService.class);
	private UserPropsMapper propsMapper;
	
	@Autowired
	public GupShupSMSService(UserPropsMapper mapper) {
		this.propsMapper = mapper;
	}
	
	/**
	 * Method handling message sending.
	 * 
	 * We will be needing a array of variables that will be sent along with the url to api. Structure :
	 * </br>
	 * 1) apiVar[0] : version of gupshup api being used : currently "1.1"
	 * </br>
	 * 2) apiVar[1] : format of message : TEXT, XML, JSON
	 * </br>
	 * 3) apiVar[2] : type of message : Text, Unicode_text, or flash, VCARD, binary
	 * </br>
	 * 4) apiVar[3] : authentication scheme : "PLAIN"(only this supported right now)
	 * </br>
	 * 
	 * @param message : actual message to be delivered.
	 * @param phoneNos : list of phone numbers.
	 * @param apiVar : String[] : consisting of params needed in api-url.
	 * @return String : url format String that will be attached to GupShup API url and then sent.
	 * @throws UnsupportedEncodingException
	 */
	public String sendSimpleSMS(String message, List<Long> phoneNos, String[] apiVar) throws IOException {
		
		log.info("[sendSimpleSMS]");
		
		String messageBody = this.messageBodyFormatter(message, phoneNos, apiVar);
		
		URL url = new URL("http://enterprise.smsgupshup.com/GatewayAPI/rest?" + messageBody);

		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.connect();
				
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		StringBuffer buffer = new StringBuffer();
		
		while ((line = rd.readLine()) != null)
			buffer.append(line).append("\n");
		
		rd.close();
		conn.disconnect();		
		
		return buffer.toString();
	}
	
	/**
	 * Method to provide String with required params for message delivery.
	 * </br>
	 * We will be needing a array of variables that will be sent along with the url to api. Structure :
	 * </br>
	 * 1) apiVar[0] : version of gupshup api being used : currently "1.1"
	 * </br>
	 * 2) apiVar[1] : format of message : TEXT, XML, JSON
	 * </br>
	 * 3) apiVar[2] : type of message : Text, Unicode_text, or flash, VCARD, binary
	 * </br>
	 * 4) apiVar[3] : authentication scheme : "PLAIN"(only this supported right now)
	 * </br>
	 * 
	 * @param message : actual message to be delivered.
	 * @param phoneNos : list of phone numbers.
	 * @param apiVar : String[] : consisting of params needed in api-url.
	 * @return String : url format String that will be attached to GupShup API url and then sent.
	 * @throws UnsupportedEncodingException
	 */
	protected String messageBodyFormatter(String message, List<Long> phoneNos, String[] apiVar) throws UnsupportedEncodingException {
	
		log.info("[messageBodyFormatter]");
		
		StringBuilder msg= new StringBuilder();
		msg.append("method=sendMessage");
		msg.append("&userid=" 		+ this.propsMapper.getId()); // your loginId
		msg.append("&password=" 	+ URLEncoder.encode(this.propsMapper.getPassword(), "UTF-8")); // your password
		msg.append("&msg=" 			+ URLEncoder.encode(message , "UTF-8"));
		msg.append("&send_to=" 		+ URLEncoder.encode( this.recepientNumberFormatter(phoneNos) , "UTF-8")); // a valid 10 digit phone no.
		msg.append("&v=" 			+ apiVar[0]);
		msg.append("&format=" 		+ apiVar[1]);
		msg.append("&msg_type=" 	+ apiVar[2]); // Can by "FLASH" or "UNICODE_TEXT" or “BINARY”
		msg.append("&auth_scheme=" 	+ apiVar[3]);
		
		return msg.toString();
	}
	
	/**
	 * Method to convert list of phone numbers into comma separated string.
	 * 
	 * @param phoneNo : list of phone numbers
	 * @return String : comma separated phone numbers
	 * @author Nitin Sharma
	 */
	protected String recepientNumberFormatter(List<Long> phoneNo) {
		
		log.info("[recepientNumberFormatter]");
		
		int size = phoneNo.size();
		StringBuilder builder = new StringBuilder();
		
		for(int i=0; i<size; i++) 
			builder.append(phoneNo.get(i)).append(",");
		builder.deleteCharAt(builder.length()-1);		
		
		return builder.toString();
	}
}
