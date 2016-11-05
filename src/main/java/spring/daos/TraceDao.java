package spring.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import spring.models.Game;
import spring.models.Trace;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Djowood on 25/10/2016.
 */

@Transactional
@RepositoryRestResource(collectionResourceRel = "trace", path = "trace")
public interface TraceDao extends CrudRepository<Trace, Long> {
}
