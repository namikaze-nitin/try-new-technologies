package co.nitin.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Mapping for `smsprovider.properties` file. Will help in fetching whole properties file as an object.
 * 
 * @author Nitin Sharma
 *
 */
@PropertySource("classpath:sms.properties")
@ConfigurationProperties(prefix = "user")
@Component
public class UserPropsMapper {

	private long id;
	private String password;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
}
