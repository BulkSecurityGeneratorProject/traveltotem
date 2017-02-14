package pl.jpetryk.traveltotem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Totem.
 */
@Entity
@Table(name = "totem")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Totem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "creation_latitude", nullable = false)
    private Double creationLatitude;

    @NotNull
    @Column(name = "creation_longitude", nullable = false)
    private Double creationLongitude;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @NotNull
    @Column(name = "distance_travelled", nullable = false)
    private Double distanceTravelled;

    @ManyToOne
    @NotNull
    private User createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCreationLatitude() {
        return creationLatitude;
    }

    public Totem creationLatitude(Double creationLatitude) {
        this.creationLatitude = creationLatitude;
        return this;
    }

    public void setCreationLatitude(Double creationLatitude) {
        this.creationLatitude = creationLatitude;
    }

    public Double getCreationLongitude() {
        return creationLongitude;
    }

    public Totem creationLongitude(Double creationLongitude) {
        this.creationLongitude = creationLongitude;
        return this;
    }

    public void setCreationLongitude(Double creationLongitude) {
        this.creationLongitude = creationLongitude;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Totem creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Double getDistanceTravelled() {
        return distanceTravelled;
    }

    public Totem distanceTravelled(Double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
        return this;
    }

    public void setDistanceTravelled(Double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public Totem createdBy(User user) {
        this.createdBy = user;
        return this;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Totem totem = (Totem) o;
        if (totem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, totem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Totem{" +
            "id=" + id +
            ", creationLatitude='" + creationLatitude + "'" +
            ", creationLongitude='" + creationLongitude + "'" +
            ", creationDate='" + creationDate + "'" +
            ", distanceTravelled='" + distanceTravelled + "'" +
            '}';
    }
}
