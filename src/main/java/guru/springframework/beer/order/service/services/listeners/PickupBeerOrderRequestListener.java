package guru.springframework.beer.order.service.services.listeners;

import guru.springframework.beer.order.service.config.JMSConfig;
import guru.springframework.beer.order.service.services.BeerOrderManager;
import guru.springframework.brewery.events.PickupBeerOrderRequest;
import guru.springframework.brewery.model.BeerOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PickupBeerOrderRequestListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JMSConfig.PICKUP_ORDER_QUEUE)
    public void listen(PickupBeerOrderRequest request) {
        BeerOrderDto beerOrderDto = request.getBeerOrderDto();

        log.debug("Pickup Beer Order request received for order #: " + beerOrderDto.getId());

        beerOrderManager.beerOrderPickUp(beerOrderDto);
    }
}
