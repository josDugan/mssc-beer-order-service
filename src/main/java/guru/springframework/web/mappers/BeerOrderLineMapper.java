package guru.springframework.web.mappers;

import guru.springframework.domain.BeerOrderLine;
import guru.springframework.web.model.BeerOrderLineDto;
import org.mapstruct.Mapper;

@Mapper(uses = DateMapper.class)
public interface BeerOrderLineMapper {

    BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line);

    BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto);
}
