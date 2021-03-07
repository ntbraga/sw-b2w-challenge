package br.com.b2w.b2wchallenge.data.repositories;

import br.com.b2w.b2wchallenge.data.entities.PlanetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PlanetRepository extends MongoRepository<PlanetEntity, String> {

    @Query("{'name': { $regex : ?0, $options: 'i' }}")
    Page<PlanetEntity> findByNameLike(String name, Pageable pageable);

    Page<PlanetEntity> findAll(Pageable pageable);

}
