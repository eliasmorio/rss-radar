package fr.emorio.repository;

import fr.emorio.model.Feed;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends PagingAndSortingRepository<Feed, Long>, CrudRepository<Feed, Long> {
}
