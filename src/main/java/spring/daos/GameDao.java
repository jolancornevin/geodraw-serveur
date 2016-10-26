package spring.daos;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import spring.models.Game;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Djowood on 25/10/2016.
 */

//http://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/#jpa.query-methods.query-creation

@Transactional
@RepositoryRestResource(collectionResourceRel = "game", path = "game")
public interface GameDao extends CrudRepository<Game, Long> {
    public List<Game> findByName(String game);
}