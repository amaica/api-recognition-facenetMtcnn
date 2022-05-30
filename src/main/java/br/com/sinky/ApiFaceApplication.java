package br.com.sinky;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import br.com.sinky.service.FaceService;

@SpringBootApplication
@EnableScheduling
public class ApiFaceApplication {

	public static void main(String[] args) {
	
		SpringApplication.run(ApiFaceApplication.class, args);
	}

}
