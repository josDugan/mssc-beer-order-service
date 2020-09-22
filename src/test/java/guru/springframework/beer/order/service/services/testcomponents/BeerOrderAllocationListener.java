package guru.springframework.beer.order.service.services.testcomponents;

import guru.springframework.beer.order.service.config.JMSConfig;
import guru.springframework.brewery.events.AllocateBeerOrderRequest;
import guru.springframework.brewery.events.AllocateBeerOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderAllocationListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JMSConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(Message msg) {
        AllocateBeerOrderRequest request = (AllocateBeerOrderRequest) msg.getPayload();
        if ("cancel-order-allocation".equals(request.getBeerOrderDto().getCustomerRef())) {
            return;
        }
        boolean allocationError = false;
        boolean pendingInventory = false;

        request.getBeerOrderDto().getBeerOrderLines().forEach(beerOrderLineDto -> {
            beerOrderLineDto.setQuantityAllocated(beerOrderLineDto.getOrderQuantity());
        });

        log.debug("test component beer order allocation listener ###########################");

        if ("fail-allocation".equals(request.getBeerOrderDto().getCustomerRef())) {
            allocationError = true;
        }

        if ("partial-allocation".equals(request.getBeerOrderDto().getCustomerRef())) {
            pendingInventory = true;
        }

        jmsTemplate.convertAndSend(JMSConfig.ALLOCATE_ORDER_RESULT_QUEUE,
                AllocateBeerOrderResponse.builder()
                        .allocationError(allocationError)
                        .pendingInventory(pendingInventory)
                        .beerOrderDto(request.getBeerOrderDto())
                        .build());
    }
}
