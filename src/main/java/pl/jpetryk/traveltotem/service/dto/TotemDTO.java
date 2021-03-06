package pl.jpetryk.traveltotem.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Totem entity.
 */
public class TotemDTO implements Serializable {

    private Long id;

    @NotNull
    private Double creationLatitude;

    @NotNull
    private Double creationLongitude;

    @NotNull
    private LocalDate creationDate;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal distanceTravelled;


    private Long createdById;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Double getCreationLatitude() {
        return creationLatitude;
    }

    public void setCreationLatitude(Double creationLatitude) {
        this.creationLatitude = creationLatitude;
    }
    public Double getCreationLongitude() {
        return creationLongitude;
    }

    public void setCreationLongitude(Double creationLongitude) {
        this.creationLongitude = creationLongitude;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
    public BigDecimal getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(BigDecimal distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TotemDTO totemDTO = (TotemDTO) o;

        if ( ! Objects.equals(id, totemDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TotemDTO{" +
            "id=" + id +
            ", creationLatitude='" + creationLatitude + "'" +
            ", creationLongitude='" + creationLongitude + "'" +
            ", creationDate='" + creationDate + "'" +
            ", distanceTravelled='" + distanceTravelled + "'" +
            '}';
    }
}
