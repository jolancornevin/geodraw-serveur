package mysql;

/**
 * Created by Djowood on 17/10/2016.
 */

import model.Game;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration("Beans.xml")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner demo(final GameRepository repository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                repository.save(new Game(1, "Kim", true, 4, 20, 12, 11, "actions"));
                repository.save(new Game(2, "Kim", true, 4, 20, 12, 11, "avions"));
                repository.save(new Game(3, "Kim", true, 4, 20, 12, 11, "lol"));

                // fetch all customers

                for (Game customer : repository.findAll()) {
                    System.out.println(customer.toString());
                }

                // fetch an individual customer by ID
                Game customer = repository.findOne(1L);
                System.out.println(customer);

                // fetch customers by last name
                for (Game bauer : repository.findByName("Bauer")) {
                    System.out.println(bauer.toString());
                }
            }
        };
    }
}
