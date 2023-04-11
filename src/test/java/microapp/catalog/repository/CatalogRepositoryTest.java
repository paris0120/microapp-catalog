package microapp.catalog.repository;

import microapp.catalog.domain.Catalog;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
class CatalogRepositoryTest {

    @Autowired
    CatalogRepository catalogRepository;


    @Test
    void findAllByGroupUuidAndTypeOrderByWeight() {
        UUID uuid = UUID.randomUUID();
        Catalog catalog = new Catalog();
        catalog.setGroupUuid(uuid);
        catalog.setName("Test");
        catalog.setType("test");
        catalogRepository.save(catalog);
        catalogRepository.save(catalog);
        catalogRepository.save(catalog);
        catalogRepository.save(catalog);
        catalogRepository.findAllGroups().collectList().map(groups-> {
            boolean output = false;
            for(String groupdUuid:groups) {
                if(groupdUuid.equals(uuid)) output =false;
            }
            assertEquals(output, true);
            return output;
        });
    }

    @Test
    void findAllGroups() {
    }

    @Test
    void findAllGroupTypes() {
    }
}
