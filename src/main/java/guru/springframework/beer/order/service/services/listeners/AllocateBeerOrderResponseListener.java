package guru.springframework.beer.order.service.services.listeners;

import guru.springframework.brewery.events.AllocateBeerOrderResponse;
import guru.springframework.brewery.model.BeerOrderDto;
import guru.springframework.beer.order.service.config.JMSConfig;
import guru.springframework.beer.order.service.services.BeerOrderManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AllocateBeerOrderResponseListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JMSConfig.ALLOCATE_ORDER_RESULT_QUEUE)
    public void listen(AllocateBeerOrderResponse allocateBeerOrderResponse) {
        BeerOrderDto beerOrderDto = allocateBeerOrderResponse.getBeerOrderDto();

        log.debug("Allocate Beer Order Response message received for order: " + beerOrderDto.getId());

        if (allocateBeerOrderResponse.getAllocationError()) {
            beerOrderManager.beerOrderAllocationFailed(beerOrderDto);
        } else if (allocateBeerOrderResponse.getPendingInventory()) {
            beerOrderManager.beerOrderAllocationPendingInventory(beerOrderDto);
        } else {
            beerOrderManager.beerOrderAllocationPassed(beerOrderDto);
        }
    }
}
