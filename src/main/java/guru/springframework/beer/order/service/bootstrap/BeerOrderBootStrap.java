package guru.springframework.beer.order.service.bootstrap;

import guru.springframework.beer.order.service.domain.Customer;
import guru.springframework.beer.order.service.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderBootStrap implements CommandLineRunner {
    public static final String TASTING_ROOM = "Tasting Room";
    public static final String BEER_1_UPC = "01234567890";
    public static final String BEER_2_UPC = "01234567891";
    public static final String BEER_3_UPC = "01234567892";

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadCustomerData();
    }

    private void loadCustomerData() {
        if (customerRepository.count() == 0) {
            Customer savedCustomer = customerRepository.save(
                    Customer.builder()
                    .customerName(TASTING_ROOM)
                    .apiKey(UUID.randomUUID())
                    .build()
            );

            log.debug("Tasting Room Customer Id: " + savedCustomer.getId().toString());
        }

    }
}

