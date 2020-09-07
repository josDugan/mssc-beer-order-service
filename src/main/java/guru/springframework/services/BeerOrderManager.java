package guru.springframework.services;

import guru.springframework.domain.BeerOrder;

import java.util.UUID;

public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);

    void processBeerOrderValidation(UUID orderId, Boolean isValid);
}
