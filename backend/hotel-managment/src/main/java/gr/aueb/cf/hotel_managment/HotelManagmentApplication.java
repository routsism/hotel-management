package gr.aueb.cf.hotel_managment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HotelManagmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelManagmentApplication.class, args);
	}

}
