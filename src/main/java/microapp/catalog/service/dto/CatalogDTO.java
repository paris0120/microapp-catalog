package microapp.catalog.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link microapp.catalog.domain.Catalog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CatalogDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String hover;

    @Lob
    private String description;

    private String color;

    private String link;

    private String icon;

    private Integer weight;

    private UUID groupUuid;

    private Boolean isActive;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHover() {
        return hover;
    }

    public void setHover(String hover) {
        this.hover = hover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public UUID getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(UUID groupUuid) {
        this.groupUuid = groupUuid;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogDTO)) {
            return false;
        }

        CatalogDTO catalogDTO = (CatalogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, catalogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", hover='" + getHover() + "'" +
            ", description='" + getDescription() + "'" +
            ", color='" + getColor() + "'" +
            ", link='" + getLink() + "'" +
            ", icon='" + getIcon() + "'" +
            ", weight=" + getWeight() +
            ", groupUuid='" + getGroupUuid() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
