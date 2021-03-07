package br.com.b2w.b2wchallenge;

import br.com.b2w.b2wchallenge.config.MongoConfig;
import br.com.b2w.b2wchallenge.config.RestConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
//@Import({MongoConfig.class, RestConfig.class})
public class B2wChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(B2wChallengeApplication.class, args);
	}

}
