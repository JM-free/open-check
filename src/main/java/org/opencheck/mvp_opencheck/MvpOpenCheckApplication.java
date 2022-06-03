package org.opencheck.mvp_opencheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MvpOpenCheckController {

	@RequestMapping("/")
	public String index() {
		return "Hello World!";
	}

	@RequestMapping("/{name}")
	public String greeting(@PathVariable String name) {
		return "Hello " + name + "!";
	}

}

@SpringBootApplication
public class MvpOpenCheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvpOpenCheckApplication.class, args);
	}

}