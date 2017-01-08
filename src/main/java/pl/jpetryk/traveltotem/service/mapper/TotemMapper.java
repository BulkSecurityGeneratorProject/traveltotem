package pl.jpetryk.traveltotem.service.mapper;

import pl.jpetryk.traveltotem.domain.*;
import pl.jpetryk.traveltotem.service.dto.TotemDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Totem and its DTO TotemDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface TotemMapper {

    @Mapping(source = "createdBy.id", target = "createdById")
    TotemDTO totemToTotemDTO(Totem totem);

    List<TotemDTO> totemsToTotemDTOs(List<Totem> totems);

    @Mapping(source = "createdById", target = "createdBy")
    Totem totemDTOToTotem(TotemDTO totemDTO);

    List<Totem> totemDTOsToTotems(List<TotemDTO> totemDTOs);
}
