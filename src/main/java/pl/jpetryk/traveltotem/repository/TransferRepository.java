package pl.jpetryk.traveltotem.repository;

import pl.jpetryk.traveltotem.domain.Transfer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Transfer entity.
 */
@SuppressWarnings("unused")
public interface TransferRepository extends JpaRepository<Transfer,Long> {

    @Query("select transfer from Transfer transfer where transfer.fromUser.login = ?#{principal.username}")
    List<Transfer> findByFromUserIsCurrentUser();

    @Query("select transfer from Transfer transfer where transfer.toUser.login = ?#{principal.username}")
    List<Transfer> findByToUserIsCurrentUser();

}
