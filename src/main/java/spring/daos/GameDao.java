package spring.daos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<Game> findByName(String game);

    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.players")
    List<Game> findAllWithPlayer();

    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.players WHERE g.id = (:id)")
    List<Game> findByIdWithPlayer(@Param("id") Long id);
}
