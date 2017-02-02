package pl.jpetryk.traveltotem.repository;

import pl.jpetryk.traveltotem.domain.Totem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Totem entity.
 */
@SuppressWarnings("unused")
public interface TotemRepository extends JpaRepository<Totem,Long> {

    @Query("select totem from Totem totem where totem.createdBy.login = ?#{principal.username}")
    List<Totem> findByCreatedByIsCurrentUser();

}
