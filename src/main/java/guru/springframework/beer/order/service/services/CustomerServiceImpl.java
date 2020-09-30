package guru.springframework.beer.order.service.services;

import guru.springframework.beer.order.service.domain.Customer;
import guru.springframework.beer.order.service.repositories.CustomerRepository;
import guru.springframework.beer.order.service.web.mappers.CustomerMapper;
import guru.springframework.brewery.model.CustomerPagedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerPagedList listCustomers(Pageable pageable) {
        log.debug("Getting list of customers");

        Page<Customer> customerPage = customerRepository.findAll(pageable);

        return new CustomerPagedList(customerPage.stream()
                .map(customer -> customerMapper.customerToDto(customer))
                .collect(Collectors.toList()),
                PageRequest.of(
                        customerPage.getPageable().getPageNumber(),
                        customerPage.getPageable().getPageSize()
                ),
                customerPage.getTotalElements());
    }
}
