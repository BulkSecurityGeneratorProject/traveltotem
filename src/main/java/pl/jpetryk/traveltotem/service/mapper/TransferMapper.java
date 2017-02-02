package pl.jpetryk.traveltotem.service.mapper;

import pl.jpetryk.traveltotem.domain.*;
import pl.jpetryk.traveltotem.service.dto.TransferDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Transfer and its DTO TransferDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, UserMapper.class, })
public interface TransferMapper {

    @Mapping(source = "fromUser.id", target = "fromUserId")
    @Mapping(source = "toUser.id", target = "toUserId")
    @Mapping(source = "totem.id", target = "totemId")
    TransferDTO transferToTransferDTO(Transfer transfer);

    List<TransferDTO> transfersToTransferDTOs(List<Transfer> transfers);

    @Mapping(source = "fromUserId", target = "fromUser")
    @Mapping(source = "toUserId", target = "toUser")
    @Mapping(source = "totemId", target = "totem")
    Transfer transferDTOToTransfer(TransferDTO transferDTO);

    List<Transfer> transferDTOsToTransfers(List<TransferDTO> transferDTOs);

    default Totem totemFromId(Long id) {
        if (id == null) {
            return null;
        }
        Totem totem = new Totem();
        totem.setId(id);
        return totem;
    }
}
