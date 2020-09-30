package guru.springframework.beer.order.service.services;

import guru.springframework.brewery.model.CustomerPagedList;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    CustomerPagedList listCustomers(Pageable pageable);
}
