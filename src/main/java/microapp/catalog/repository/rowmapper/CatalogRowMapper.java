package microapp.catalog.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import microapp.catalog.domain.Catalog;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Catalog}, with proper type conversions.
 */
@Service
public class CatalogRowMapper implements BiFunction<Row, String, Catalog> {

    private final ColumnConverter converter;

    public CatalogRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Catalog} stored in the database.
     */
    @Override
    public Catalog apply(Row row, String prefix) {
        Catalog entity = new Catalog();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setHover(converter.fromRow(row, prefix + "_hover", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setColor(converter.fromRow(row, prefix + "_color", String.class));
        entity.setLink(converter.fromRow(row, prefix + "_link", String.class));
        entity.setIcon(converter.fromRow(row, prefix + "_icon", String.class));
        entity.setWeight(converter.fromRow(row, prefix + "_weight", Integer.class));
        entity.setGroupUuid(converter.fromRow(row, prefix + "_group_uuid", UUID.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        return entity;
    }
}
