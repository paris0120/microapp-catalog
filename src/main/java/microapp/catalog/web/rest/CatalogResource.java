package microapp.catalog.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import microapp.catalog.repository.CatalogRepository;
import microapp.catalog.service.CatalogService;
import microapp.catalog.service.dto.CatalogDTO;
import microapp.catalog.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link microapp.catalog.domain.Catalog}.
 */
@RestController
@RequestMapping("/api")
public class CatalogResource {

    private final Logger log = LoggerFactory.getLogger(CatalogResource.class);

    private static final String ENTITY_NAME = "catalogCatalog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogService catalogService;

    private final CatalogRepository catalogRepository;

    public CatalogResource(CatalogService catalogService, CatalogRepository catalogRepository) {
        this.catalogService = catalogService;
        this.catalogRepository = catalogRepository;
    }

    @GetMapping("/catalogs/{groupuuid}/{type}")
    public Mono<ResponseEntity<List<CatalogDTO>>> getGroupTypeCatalog(@PathVariable UUID groupuuid, @PathVariable String type) {
//        return catalogService.findAll(groupuuid, type);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("groupuuid", groupuuid.toString());
        responseHeaders.set("type", type);
        return catalogService.findAll(groupuuid, type).collectList()
            .map(entities ->
                ResponseEntity
                    .ok()
                    .headers(responseHeaders)
                    .body(entities)
            );


    }

    @GetMapping("/catalogs/groups")
    public Mono<List<String>> getGroups() {
        return catalogService.findAllGroups().collectList();
    }
    @GetMapping("/catalogs/groups/{grouopUuid}")
    public Mono<List<String>> getGroupTypes(@PathVariable UUID grouopUuid) {

        return catalogService.findAllGroupTypes(grouopUuid).collectList();
    }


    /**
     * {@code POST  /catalogs} : Create a new catalog.
     *
     * @param catalogDTO the catalogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogDTO, or with status {@code 400 (Bad Request)} if the catalog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalogs")
    public Mono<ResponseEntity<CatalogDTO>> createCatalog(@Valid @RequestBody CatalogDTO catalogDTO) throws URISyntaxException {
        log.debug("REST request to save Catalog : {}", catalogDTO);
        if (catalogDTO.getId() != null) {
            throw new BadRequestAlertException("A new catalog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return catalogService
            .save(catalogDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/catalogs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /catalogs/:id} : Updates an existing catalog.
     *
     * @param id the id of the catalogDTO to save.
     * @param catalogDTO the catalogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogDTO,
     * or with status {@code 400 (Bad Request)} if the catalogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalogs/{id}")
    public Mono<ResponseEntity<CatalogDTO>> updateCatalog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogDTO catalogDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Catalog : {}, {}", id, catalogDTO);
        if (catalogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return catalogRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return catalogService
                    .update(catalogDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /catalogs/:id} : Partial updates given fields of an existing catalog, field will ignore if it is null
     *
     * @param id the id of the catalogDTO to save.
     * @param catalogDTO the catalogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogDTO,
     * or with status {@code 400 (Bad Request)} if the catalogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the catalogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalogs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CatalogDTO>> partialUpdateCatalog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogDTO catalogDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Catalog partially : {}, {}", id, catalogDTO);
        if (catalogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return catalogRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CatalogDTO> result = catalogService.partialUpdate(catalogDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /catalogs} : get all the catalogs.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogs in body.
     */
    @GetMapping("/catalogs")
    public Mono<ResponseEntity<List<CatalogDTO>>> getAllCatalogs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Catalogs");
        return catalogService
            .countAll()
            .zipWith(catalogService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /catalogs/:id} : get the "id" catalog.
     *
     * @param id the id of the catalogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalogs/{id}")
    public Mono<ResponseEntity<CatalogDTO>> getCatalog(@PathVariable Long id) {
        log.debug("REST request to get Catalog : {}", id);
        Mono<CatalogDTO> catalogDTO = catalogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogDTO);
    }

    /**
     * {@code DELETE  /catalogs/:id} : delete the "id" catalog.
     *
     * @param id the id of the catalogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalogs/{id}")
    public Mono<ResponseEntity<Void>> deleteCatalog(@PathVariable Long id) {
        log.debug("REST request to delete Catalog : {}", id);
        return catalogService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
