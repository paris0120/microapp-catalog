package microapp.catalog.service.mapper;

import microapp.catalog.domain.Catalog;
import microapp.catalog.service.dto.CatalogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Catalog} and its DTO {@link CatalogDTO}.
 */
@Mapper(componentModel = "spring")
public interface CatalogMapper extends EntityMapper<CatalogDTO, Catalog> {}
