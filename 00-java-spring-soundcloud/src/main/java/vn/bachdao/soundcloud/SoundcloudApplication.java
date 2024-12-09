package vn.bachdao.soundcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// unable spring security
@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class })

// @SpringBootApplication
public class SoundcloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoundcloudApplication.class, args);
	}

}
