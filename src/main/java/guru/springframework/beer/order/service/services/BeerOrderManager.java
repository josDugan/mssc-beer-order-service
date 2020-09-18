package guru.springframework.beer.order.service.services;

import guru.springframework.brewery.model.BeerOrderDto;
import guru.springframework.beer.order.service.domain.BeerOrder;

import java.util.UUID;

public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);

    void processBeerOrderValidation(UUID orderId, Boolean isValid);

    void beerOrderAllocationPassed(BeerOrderDto beerOrderDto);

    void beerOrderAllocationPendingInventory(BeerOrderDto beerOrderDto);

    void beerOrderAllocationFailed(BeerOrderDto beerOrderDto);
}
