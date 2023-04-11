package microapp.catalog.service;

import microapp.catalog.domain.Catalog;
import microapp.catalog.repository.CatalogRepository;
import microapp.catalog.service.dto.CatalogDTO;
import microapp.catalog.service.mapper.CatalogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service Implementation for managing {@link Catalog}.
 */
@Service
@Transactional
public class CatalogService {

    private final Logger log = LoggerFactory.getLogger(CatalogService.class);

    private final CatalogRepository catalogRepository;

    private final CatalogMapper catalogMapper;

    public CatalogService(CatalogRepository catalogRepository, CatalogMapper catalogMapper) {
        this.catalogRepository = catalogRepository;
        this.catalogMapper = catalogMapper;
    }

    /**
     * Save a catalog.
     *
     * @param catalogDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CatalogDTO> save(CatalogDTO catalogDTO) {
        log.debug("Request to save Catalog : {}", catalogDTO);
        return catalogRepository.save(catalogMapper.toEntity(catalogDTO)).map(catalogMapper::toDto);
    }

    /**
     * Update a catalog.
     *
     * @param catalogDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CatalogDTO> update(CatalogDTO catalogDTO) {
        log.debug("Request to update Catalog : {}", catalogDTO);
        return catalogRepository.save(catalogMapper.toEntity(catalogDTO)).map(catalogMapper::toDto);
    }

    /**
     * Partially update a catalog.
     *
     * @param catalogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CatalogDTO> partialUpdate(CatalogDTO catalogDTO) {
        log.debug("Request to partially update Catalog : {}", catalogDTO);

        return catalogRepository
            .findById(catalogDTO.getId())
            .map(existingCatalog -> {
                catalogMapper.partialUpdate(existingCatalog, catalogDTO);

                return existingCatalog;
            })
            .flatMap(catalogRepository::save)
            .map(catalogMapper::toDto);
    }

    /**
     * Get all the catalogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CatalogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Catalogs");
        return catalogRepository.findAllBy(pageable).map(catalogMapper::toDto);
    }


    /**
     * get all catalogs from group and type
     * @param groupUuid
     * @param type
     * @return
     */
    @Transactional(readOnly = true)
    public Flux<CatalogDTO> findAll(UUID groupUuid, String type) {
        log.debug("Request to get all Catalogs for group {}, {}", groupUuid, type);
        return catalogRepository.findAllByGroupUuidAndTypeOrderByWeight(groupUuid, type).map(catalogMapper::toDto);
    }


    /**
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Flux<String> findAllGroups() {
        log.debug("Request to get all Catalog groups.");
        return catalogRepository.findAllGroups();
    }


    /**
     *
     * @param groupUuid
     * @return
     */
    @Transactional(readOnly = true)
    public Flux<String> findAllGroupTypes(UUID groupUuid) {
        log.debug("Request to get all Types for Catalog group {}.", groupUuid);
        return catalogRepository.findAllGroupTypes(groupUuid);
    }


    /**
     * Returns the number of catalogs available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return catalogRepository.count();
    }

    /**
     * Get one catalog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CatalogDTO> findOne(Long id) {
        log.debug("Request to get Catalog : {}", id);
        return catalogRepository.findById(id).map(catalogMapper::toDto);
    }

    /**
     * Delete the catalog by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Catalog : {}", id);
        return catalogRepository.deleteById(id);
    }
}
