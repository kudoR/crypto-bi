package eu.ffs.crypto.bi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoBiApplication {

	public static void main(String[] args) {
		System.out.println("new Version");
		SpringApplication.run(CryptoBiApplication.class, args);
	}
}
