package guru.springframework.beer.order.service.web.mappers;

import guru.springframework.beer.order.service.domain.Customer;
import guru.springframework.brewery.model.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = DateMapper.class)
public interface CustomerMapper {

    Customer dtoToCustomer(CustomerDto customerDto);

    @Mapping(target = "name", source = "customerName")
    CustomerDto customerToDto(Customer customer);
}
