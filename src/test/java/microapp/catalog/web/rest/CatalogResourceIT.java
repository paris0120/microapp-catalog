package microapp.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import microapp.catalog.IntegrationTest;
import microapp.catalog.domain.Catalog;
import microapp.catalog.repository.CatalogRepository;
import microapp.catalog.repository.EntityManager;
import microapp.catalog.service.dto.CatalogDTO;
import microapp.catalog.service.mapper.CatalogMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CatalogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CatalogResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_HOVER = "AAAAAAAAAA";
    private static final String UPDATED_HOVER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final UUID DEFAULT_GROUP_UUID = UUID.randomUUID();
    private static final UUID UPDATED_GROUP_UUID = UUID.randomUUID();

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/catalogs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private CatalogMapper catalogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Catalog catalog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalog createEntity(EntityManager em) {
        Catalog catalog = new Catalog()
            .name(DEFAULT_NAME)
            .hover(DEFAULT_HOVER)
            .description(DEFAULT_DESCRIPTION)
            .color(DEFAULT_COLOR)
            .link(DEFAULT_LINK)
            .icon(DEFAULT_ICON)
            .weight(DEFAULT_WEIGHT)
            .groupUuid(DEFAULT_GROUP_UUID)
            .isActive(DEFAULT_IS_ACTIVE)
            .type(DEFAULT_TYPE);
        return catalog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalog createUpdatedEntity(EntityManager em) {
        Catalog catalog = new Catalog()
            .name(UPDATED_NAME)
            .hover(UPDATED_HOVER)
            .description(UPDATED_DESCRIPTION)
            .color(UPDATED_COLOR)
            .link(UPDATED_LINK)
            .icon(UPDATED_ICON)
            .weight(UPDATED_WEIGHT)
            .groupUuid(UPDATED_GROUP_UUID)
            .isActive(UPDATED_IS_ACTIVE)
            .type(UPDATED_TYPE);
        return catalog;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Catalog.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        catalog = createEntity(em);
    }

    @Test
    void createCatalog() throws Exception {
        int databaseSizeBeforeCreate = catalogRepository.findAll().collectList().block().size();
        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll().collectList().block();
        assertThat(catalogList).hasSize(databaseSizeBeforeCreate + 1);
        Catalog testCatalog = catalogList.get(catalogList.size() - 1);
        assertThat(testCatalog.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatalog.getHover()).isEqualTo(DEFAULT_HOVER);
        assertThat(testCatalog.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCatalog.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testCatalog.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testCatalog.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testCatalog.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testCatalog.getGroupUuid()).isEqualTo(DEFAULT_GROUP_UUID);
        assertThat(testCatalog.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testCatalog.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createCatalogWithExistingId() throws Exception {
        // Create the Catalog with an existing ID
        catalog.setId(1L);
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        int databaseSizeBeforeCreate = catalogRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll().collectList().block();
        assertThat(catalogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogRepository.findAll().collectList().block().size();
        // set the field null
        catalog.setName(null);

        // Create the Catalog, which fails.
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Catalog> catalogList = catalogRepository.findAll().collectList().block();
        assertThat(catalogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCatalogs() {
        // Initialize the database
        catalogRepository.save(catalog).block();

        // Get all the catalogList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(catalog.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].hover")
            .value(hasItem(DEFAULT_HOVER))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION.toString()))
            .jsonPath("$.[*].color")
            .value(hasItem(DEFAULT_COLOR))
            .jsonPath("$.[*].link")
            .value(hasItem(DEFAULT_LINK))
            .jsonPath("$.[*].icon")
            .value(hasItem(DEFAULT_ICON))
            .jsonPath("$.[*].weight")
            .value(hasItem(DEFAULT_WEIGHT))
            .jsonPath("$.[*].groupUuid")
            .value(hasItem(DEFAULT_GROUP_UUID.toString()))
            .jsonPath("$.[*].isActive")
            .value(hasItem(DEFAULT_IS_ACTIVE.booleanValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE));
    }

    @Test
    void getCatalog() {
        // Initialize the database
        catalogRepository.save(catalog).block();

        // Get the catalog
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, catalog.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(catalog.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.hover")
            .value(is(DEFAULT_HOVER))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION.toString()))
            .jsonPath("$.color")
            .value(is(DEFAULT_COLOR))
            .jsonPath("$.link")
            .value(is(DEFAULT_LINK))
            .jsonPath("$.icon")
            .value(is(DEFAULT_ICON))
            .jsonPath("$.weight")
            .value(is(DEFAULT_WEIGHT))
            .jsonPath("$.groupUuid")
            .value(is(DEFAULT_GROUP_UUID.toString()))
            .jsonPath("$.isActive")
            .value(is(DEFAULT_IS_ACTIVE.booleanValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE));
    }

    @Test
    void getNonExistingCatalog() {
        // Get the catalog
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCatalog() throws Exception {
        // Initialize the database
        catalogRepository.save(catalog).block();

        int databaseSizeBeforeUpdate = catalogRepository.findAll().collectList().block().size();

        // Update the catalog
        Catalog updatedCatalog = catalogRepository.findById(catalog.getId()).block();
        updatedCatalog
            .name(UPDATED_NAME)
            .hover(UPDATED_HOVER)
            .description(UPDATED_DESCRIPTION)
            .color(UPDATED_COLOR)
            .link(UPDATED_LINK)
            .icon(UPDATED_ICON)
            .weight(UPDATED_WEIGHT)
            .groupUuid(UPDATED_GROUP_UUID)
            .isActive(UPDATED_IS_ACTIVE)
            .type(UPDATED_TYPE);
        CatalogDTO catalogDTO = catalogMapper.toDto(updatedCatalog);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, catalogDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll().collectList().block();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
        Catalog testCatalog = catalogList.get(catalogList.size() - 1);
        assertThat(testCatalog.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalog.getHover()).isEqualTo(UPDATED_HOVER);
        assertThat(testCatalog.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalog.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testCatalog.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testCatalog.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testCatalog.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testCatalog.getGroupUuid()).isEqualTo(UPDATED_GROUP_UUID);
        assertThat(testCatalog.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testCatalog.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingCatalog() throws Exception {
        int databaseSizeBeforeUpdate = catalogRepository.findAll().collectList().block().size();
        catalog.setId(count.incrementAndGet());

        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, catalogDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll().collectList().block();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCatalog() throws Exception {
        int databaseSizeBeforeUpdate = catalogRepository.findAll().collectList().block().size();
        catalog.setId(count.incrementAndGet());

        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll().collectList().block();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCatalog() throws Exception {
        int databaseSizeBeforeUpdate = catalogRepository.findAll().collectList().block().size();
        catalog.setId(count.incrementAndGet());

        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll().collectList().block();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCatalogWithPatch() throws Exception {
        // Initialize the database
        catalogRepository.save(catalog).block();

        int databaseSizeBeforeUpdate = catalogRepository.findAll().collectList().block().size();

        // Update the catalog using partial update
        Catalog partialUpdatedCatalog = new Catalog();
        partialUpdatedCatalog.setId(catalog.getId());

        partialUpdatedCatalog
            .description(UPDATED_DESCRIPTION)
            .color(UPDATED_COLOR)
            .link(UPDATED_LINK)
            .weight(UPDATED_WEIGHT)
            .isActive(UPDATED_IS_ACTIVE)
            .type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCatalog.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalog))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll().collectList().block();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
        Catalog testCatalog = catalogList.get(catalogList.size() - 1);
        assertThat(testCatalog.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatalog.getHover()).isEqualTo(DEFAULT_HOVER);
        assertThat(testCatalog.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalog.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testCatalog.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testCatalog.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testCatalog.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testCatalog.getGroupUuid()).isEqualTo(DEFAULT_GROUP_UUID);
        assertThat(testCatalog.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testCatalog.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void fullUpdateCatalogWithPatch() throws Exception {
        // Initialize the database
        catalogRepository.save(catalog).block();

        int databaseSizeBeforeUpdate = catalogRepository.findAll().collectList().block().size();

        // Update the catalog using partial update
        Catalog partialUpdatedCatalog = new Catalog();
        partialUpdatedCatalog.setId(catalog.getId());

        partialUpdatedCatalog
            .name(UPDATED_NAME)
            .hover(UPDATED_HOVER)
            .description(UPDATED_DESCRIPTION)
            .color(UPDATED_COLOR)
            .link(UPDATED_LINK)
            .icon(UPDATED_ICON)
            .weight(UPDATED_WEIGHT)
            .groupUuid(UPDATED_GROUP_UUID)
            .isActive(UPDATED_IS_ACTIVE)
            .type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCatalog.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalog))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll().collectList().block();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
        Catalog testCatalog = catalogList.get(catalogList.size() - 1);
        assertThat(testCatalog.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalog.getHover()).isEqualTo(UPDATED_HOVER);
        assertThat(testCatalog.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalog.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testCatalog.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testCatalog.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testCatalog.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testCatalog.getGroupUuid()).isEqualTo(UPDATED_GROUP_UUID);
        assertThat(testCatalog.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testCatalog.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingCatalog() throws Exception {
        int databaseSizeBeforeUpdate = catalogRepository.findAll().collectList().block().size();
        catalog.setId(count.incrementAndGet());

        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, catalogDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll().collectList().block();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCatalog() throws Exception {
        int databaseSizeBeforeUpdate = catalogRepository.findAll().collectList().block().size();
        catalog.setId(count.incrementAndGet());

        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll().collectList().block();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCatalog() throws Exception {
        int databaseSizeBeforeUpdate = catalogRepository.findAll().collectList().block().size();
        catalog.setId(count.incrementAndGet());

        // Create the Catalog
        CatalogDTO catalogDTO = catalogMapper.toDto(catalog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(catalogDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Catalog in the database
        List<Catalog> catalogList = catalogRepository.findAll().collectList().block();
        assertThat(catalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCatalog() {
        // Initialize the database
        catalogRepository.save(catalog).block();

        int databaseSizeBeforeDelete = catalogRepository.findAll().collectList().block().size();

        // Delete the catalog
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, catalog.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Catalog> catalogList = catalogRepository.findAll().collectList().block();
        assertThat(catalogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
