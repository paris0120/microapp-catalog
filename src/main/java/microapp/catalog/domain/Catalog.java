package microapp.catalog.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Catalog.
 */
@Table("catalog")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Catalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("hover")
    private String hover;

    @Column("description")
    private String description;

    @Column("color")
    private String color;

    @Column("link")
    private String link;

    @Column("icon")
    private String icon;

    @Column("weight")
    private Integer weight;

    @Column("group_uuid")
    private UUID groupUuid;

    @Column("is_active")
    private Boolean isActive;

    @Column("type")
    private String type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Catalog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Catalog name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHover() {
        return this.hover;
    }

    public Catalog hover(String hover) {
        this.setHover(hover);
        return this;
    }

    public void setHover(String hover) {
        this.hover = hover;
    }

    public String getDescription() {
        return this.description;
    }

    public Catalog description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return this.color;
    }

    public Catalog color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLink() {
        return this.link;
    }

    public Catalog link(String link) {
        this.setLink(link);
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIcon() {
        return this.icon;
    }

    public Catalog icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public Catalog weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public UUID getGroupUuid() {
        return this.groupUuid;
    }

    public Catalog groupUuid(UUID groupUuid) {
        this.setGroupUuid(groupUuid);
        return this;
    }

    public void setGroupUuid(UUID groupUuid) {
        this.groupUuid = groupUuid;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Catalog isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getType() {
        return this.type;
    }

    public Catalog type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Catalog)) {
            return false;
        }
        return id != null && id.equals(((Catalog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Catalog{" +
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
