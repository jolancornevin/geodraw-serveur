package spring; /**
 * Created by Djowood on 17/10/2016.
 */

import org.hibernate.cfg.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import spring.communication.Server;
import spring.controllers.GameController;
import spring.controllers.PlayerController;
import spring.models.Player;
import spring.utils.Connexion;

import java.util.Scanner;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Configuration cfg = new Configuration();
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");

        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(PlayerController playerController, GameController gameController) {
        return (args) -> {
            //set connexion variables
            Connexion.setInstance(playerController, playerController.getPlayerDao(), gameController, gameController.getGameDao());

            //Instanciation du serveur
            Server s = new Server();
            //Démarrage et chargement des données
            s.start();
            System.out.println("Serveur socket démmarer");

            @SuppressWarnings("resource")
            Scanner sc = new Scanner(System.in);
            sc.nextLine();

            s.stop();

            System.out.println("Serveur socket stopper");
        };
    }
}