package pl.jpetryk.traveltotem.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import pl.jpetryk.traveltotem.domain.enumeration.TransferStatus;

/**
 * A DTO for the Transfer entity.
 */
public class TransferDTO implements Serializable {

    private Long id;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private TransferStatus status;

    @NotNull
    private LocalDate date;


    private Long fromUserId;
    
    private Long toUserId;
    
    private Long totemId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long userId) {
        this.fromUserId = userId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long userId) {
        this.toUserId = userId;
    }

    public Long getTotemId() {
        return totemId;
    }

    public void setTotemId(Long totemId) {
        this.totemId = totemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransferDTO transferDTO = (TransferDTO) o;

        if ( ! Objects.equals(id, transferDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TransferDTO{" +
            "id=" + id +
            ", latitude='" + latitude + "'" +
            ", longitude='" + longitude + "'" +
            ", status='" + status + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
