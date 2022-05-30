package br.com.sinky.apirecognition;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.SpringApplication;

import br.com.sinky.ApiFaceApplication;

class ApiFaceApplicationTests {
	public static void main(String[] args) throws IOException {
		
		String path = new File(".").getCanonicalPath();
		System.out.println(path);
	}
	
}
