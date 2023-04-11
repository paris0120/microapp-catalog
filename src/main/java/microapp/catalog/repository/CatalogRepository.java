package microapp.catalog.repository;

import microapp.catalog.domain.Catalog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Spring Data R2DBC repository for the Catalog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogRepository extends ReactiveCrudRepository<Catalog, Long>, CatalogRepositoryInternal {
    Flux<Catalog> findAllBy(Pageable pageable);

    @Override
    <S extends Catalog> Mono<S> save(S entity);

    @Override
    Flux<Catalog> findAll();

    Flux<Catalog> findAllByGroupUuidAndTypeOrderByWeight(UUID groupUuid, String type);

    @Query("select distinct a.group_uuid from catalog a")
    Flux<String> findAllGroups();
    @Query("select distinct a.type from catalog a where a.group_uuid = ?1")
    Flux<String> findAllGroupTypes(UUID uuid);
    @Override
    Mono<Catalog> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CatalogRepositoryInternal {
    <S extends Catalog> Mono<S> save(S entity);

    Flux<Catalog> findAllBy(Pageable pageable);

    Flux<Catalog> findAll();

    Mono<Catalog> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Catalog> findAllBy(Pageable pageable, Criteria criteria);

}
