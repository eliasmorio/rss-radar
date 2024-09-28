package fr.emorio.repository;

import fr.emorio.model.Feed;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.net.URL;

@Repository
public interface FeedRepository extends CrudRepository<Feed, Long> {
    boolean existsByUrl(String url);
}
