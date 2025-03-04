package com.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ServerApplication {

	@RequestMapping("/")
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
