package pl.jpetryk.traveltotem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import pl.jpetryk.traveltotem.domain.enumeration.TransferStatus;

/**
 * A Transfer.
 */
@Entity
@Table(name = "transfer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransferStatus status;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @NotNull
    private User fromUser;

    @ManyToOne
    @NotNull
    private User toUser;

    @ManyToOne
    @NotNull
    private Totem totem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Transfer latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Transfer longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public Transfer status(TransferStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public Transfer date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getFromUser() {
        return fromUser;
    }

    public Transfer fromUser(User user) {
        this.fromUser = user;
        return this;
    }

    public void setFromUser(User user) {
        this.fromUser = user;
    }

    public User getToUser() {
        return toUser;
    }

    public Transfer toUser(User user) {
        this.toUser = user;
        return this;
    }

    public void setToUser(User user) {
        this.toUser = user;
    }

    public Totem getTotem() {
        return totem;
    }

    public Transfer totem(Totem totem) {
        this.totem = totem;
        return this;
    }

    public void setTotem(Totem totem) {
        this.totem = totem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transfer transfer = (Transfer) o;
        if (transfer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, transfer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Transfer{" +
            "id=" + id +
            ", latitude='" + latitude + "'" +
            ", longitude='" + longitude + "'" +
            ", status='" + status + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
