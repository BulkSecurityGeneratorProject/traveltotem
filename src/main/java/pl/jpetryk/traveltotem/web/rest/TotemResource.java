package pl.jpetryk.traveltotem.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.jpetryk.traveltotem.service.TotemService;
import pl.jpetryk.traveltotem.web.rest.util.HeaderUtil;
import pl.jpetryk.traveltotem.web.rest.util.PaginationUtil;
import pl.jpetryk.traveltotem.service.dto.TotemDTO;

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
 * REST controller for managing Totem.
 */
@RestController
@RequestMapping("/api")
public class TotemResource {

    private final Logger log = LoggerFactory.getLogger(TotemResource.class);
        
    @Inject
    private TotemService totemService;

    /**
     * POST  /totems : Create a new totem.
     *
     * @param totemDTO the totemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new totemDTO, or with status 400 (Bad Request) if the totem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/totems")
    @Timed
    public ResponseEntity<TotemDTO> createTotem(@Valid @RequestBody TotemDTO totemDTO) throws URISyntaxException {
        log.debug("REST request to save Totem : {}", totemDTO);
        if (totemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("totem", "idexists", "A new totem cannot already have an ID")).body(null);
        }
        TotemDTO result = totemService.save(totemDTO);
        return ResponseEntity.created(new URI("/api/totems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("totem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /totems : Updates an existing totem.
     *
     * @param totemDTO the totemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated totemDTO,
     * or with status 400 (Bad Request) if the totemDTO is not valid,
     * or with status 500 (Internal Server Error) if the totemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/totems")
    @Timed
    public ResponseEntity<TotemDTO> updateTotem(@Valid @RequestBody TotemDTO totemDTO) throws URISyntaxException {
        log.debug("REST request to update Totem : {}", totemDTO);
        if (totemDTO.getId() == null) {
            return createTotem(totemDTO);
        }
        TotemDTO result = totemService.save(totemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("totem", totemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /totems : get all the totems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of totems in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/totems")
    @Timed
    public ResponseEntity<List<TotemDTO>> getAllTotems(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Totems");
        Page<TotemDTO> page = totemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/totems");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /totems/:id : get the "id" totem.
     *
     * @param id the id of the totemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the totemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/totems/{id}")
    @Timed
    public ResponseEntity<TotemDTO> getTotem(@PathVariable Long id) {
        log.debug("REST request to get Totem : {}", id);
        TotemDTO totemDTO = totemService.findOne(id);
        return Optional.ofNullable(totemDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /totems/:id : delete the "id" totem.
     *
     * @param id the id of the totemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/totems/{id}")
    @Timed
    public ResponseEntity<Void> deleteTotem(@PathVariable Long id) {
        log.debug("REST request to delete Totem : {}", id);
        totemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("totem", id.toString())).build();
    }

}
