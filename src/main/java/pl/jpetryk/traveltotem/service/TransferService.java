package pl.jpetryk.traveltotem.service;

import pl.jpetryk.traveltotem.domain.Transfer;
import pl.jpetryk.traveltotem.repository.TransferRepository;
import pl.jpetryk.traveltotem.service.dto.TransferDTO;
import pl.jpetryk.traveltotem.service.mapper.TransferMapper;
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
 * Service Implementation for managing Transfer.
 */
@Service
@Transactional
public class TransferService {

    private final Logger log = LoggerFactory.getLogger(TransferService.class);
    
    @Inject
    private TransferRepository transferRepository;

    @Inject
    private TransferMapper transferMapper;

    /**
     * Save a transfer.
     *
     * @param transferDTO the entity to save
     * @return the persisted entity
     */
    public TransferDTO save(TransferDTO transferDTO) {
        log.debug("Request to save Transfer : {}", transferDTO);
        Transfer transfer = transferMapper.transferDTOToTransfer(transferDTO);
        transfer = transferRepository.save(transfer);
        TransferDTO result = transferMapper.transferToTransferDTO(transfer);
        return result;
    }

    /**
     *  Get all the transfers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TransferDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transfers");
        Page<Transfer> result = transferRepository.findAll(pageable);
        return result.map(transfer -> transferMapper.transferToTransferDTO(transfer));
    }

    /**
     *  Get one transfer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TransferDTO findOne(Long id) {
        log.debug("Request to get Transfer : {}", id);
        Transfer transfer = transferRepository.findOne(id);
        TransferDTO transferDTO = transferMapper.transferToTransferDTO(transfer);
        return transferDTO;
    }

    /**
     *  Delete the  transfer by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Transfer : {}", id);
        transferRepository.delete(id);
    }
}
