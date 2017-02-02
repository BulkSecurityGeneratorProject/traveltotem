package pl.jpetryk.traveltotem.service;

import pl.jpetryk.traveltotem.domain.Totem;
import pl.jpetryk.traveltotem.repository.TotemRepository;
import pl.jpetryk.traveltotem.service.dto.TotemDTO;
import pl.jpetryk.traveltotem.service.mapper.TotemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Totem.
 */
@Service
@Transactional
public class TotemService {

    private final Logger log = LoggerFactory.getLogger(TotemService.class);
    
    @Inject
    private TotemRepository totemRepository;

    @Inject
    private TotemMapper totemMapper;

    /**
     * Save a totem.
     *
     * @param totemDTO the entity to save
     * @return the persisted entity
     */
    public TotemDTO save(TotemDTO totemDTO) {
        log.debug("Request to save Totem : {}", totemDTO);
        Totem totem = totemMapper.totemDTOToTotem(totemDTO);
        totem = totemRepository.save(totem);
        TotemDTO result = totemMapper.totemToTotemDTO(totem);
        return result;
    }

    /**
     *  Get all the totems.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TotemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Totems");
        Page<Totem> result = totemRepository.findAll(pageable);
        return result.map(totem -> totemMapper.totemToTotemDTO(totem));
    }

    /**
     *  Get one totem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TotemDTO findOne(Long id) {
        log.debug("Request to get Totem : {}", id);
        Totem totem = totemRepository.findOne(id);
        TotemDTO totemDTO = totemMapper.totemToTotemDTO(totem);
        return totemDTO;
    }

    /**
     *  Delete the  totem by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Totem : {}", id);
        totemRepository.delete(id);
    }
}
