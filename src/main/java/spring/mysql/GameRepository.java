package spring.mysql;

import spring.model.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Djowood on 17/10/2016.
 */
public interface GameRepository extends CrudRepository<Game, Long> {
    List<Game> findByName(String lastName);
    List<Game> findByTheme(String theme);
}