package pl.jpetryk.traveltotem.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.jpetryk.traveltotem.service.TransferService;
import pl.jpetryk.traveltotem.web.rest.util.HeaderUtil;
import pl.jpetryk.traveltotem.web.rest.util.PaginationUtil;
import pl.jpetryk.traveltotem.service.dto.TransferDTO;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Transfer.
 */
@RestController
@RequestMapping("/api")
public class TransferResource {

    private final Logger log = LoggerFactory.getLogger(TransferResource.class);
        
    @Inject
    private TransferService transferService;

    /**
     * POST  /transfers : Create a new transfer.
     *
     * @param transferDTO the transferDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transferDTO, or with status 400 (Bad Request) if the transfer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transfers")
    @Timed
    public ResponseEntity<TransferDTO> createTransfer(@Valid @RequestBody TransferDTO transferDTO) throws URISyntaxException {
        log.debug("REST request to save Transfer : {}", transferDTO);
        if (transferDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("transfer", "idexists", "A new transfer cannot already have an ID")).body(null);
        }
        TransferDTO result = transferService.save(transferDTO);
        return ResponseEntity.created(new URI("/api/transfers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("transfer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transfers : Updates an existing transfer.
     *
     * @param transferDTO the transferDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transferDTO,
     * or with status 400 (Bad Request) if the transferDTO is not valid,
     * or with status 500 (Internal Server Error) if the transferDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transfers")
    @Timed
    public ResponseEntity<TransferDTO> updateTransfer(@Valid @RequestBody TransferDTO transferDTO) throws URISyntaxException {
        log.debug("REST request to update Transfer : {}", transferDTO);
        if (transferDTO.getId() == null) {
            return createTransfer(transferDTO);
        }
        TransferDTO result = transferService.save(transferDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("transfer", transferDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transfers : get all the transfers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transfers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/transfers")
    @Timed
    public ResponseEntity<List<TransferDTO>> getAllTransfers(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Transfers");
        Page<TransferDTO> page = transferService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transfers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transfers/:id : get the "id" transfer.
     *
     * @param id the id of the transferDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transferDTO, or with status 404 (Not Found)
     */
    @GetMapping("/transfers/{id}")
    @Timed
    public ResponseEntity<TransferDTO> getTransfer(@PathVariable Long id) {
        log.debug("REST request to get Transfer : {}", id);
        TransferDTO transferDTO = transferService.findOne(id);
        return Optional.ofNullable(transferDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /transfers/:id : delete the "id" transfer.
     *
     * @param id the id of the transferDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transfers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransfer(@PathVariable Long id) {
        log.debug("REST request to delete Transfer : {}", id);
        transferService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("transfer", id.toString())).build();
    }

}
