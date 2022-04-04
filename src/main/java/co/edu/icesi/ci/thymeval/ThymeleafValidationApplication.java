package co.edu.icesi.ci.thymeval;

import java.time.LocalDate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import co.edu.icesi.ci.thymeval.model.User;
import co.edu.icesi.ci.thymeval.model.UserGender;
import co.edu.icesi.ci.thymeval.model.UserType;
import co.edu.icesi.ci.thymeval.service.UserServiceImpl;

@SpringBootApplication
public class ThymeleafValidationApplication {

	@Bean
	public Java8TimeDialect java8TimeDialect() {
		return new Java8TimeDialect();
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext c = SpringApplication.run(ThymeleafValidationApplication.class, args);
		
		UserServiceImpl us = c.getBean(UserServiceImpl.class);
		User user1 = new User();
		user1.setUsername("juancalderon");
		user1.setPassword("123456789");
		user1.setBirthDate(LocalDate.of(2001, 2, 7));
		user1.setName("Juanx");
		user1.setEmail("juan@correo.com");
		user1.setType(UserType.patient);
		user1.setGender(UserGender.masculine);
		
		us.save(user1);
	}

}
