package spring; /**
 * Created by Djowood on 17/10/2016.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.hibernate.cfg.Configuration;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Configuration cfg = new Configuration();
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");

        SpringApplication.run(Application.class, args);
    }
}