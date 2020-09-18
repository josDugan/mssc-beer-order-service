package guru.springframework.beer.order.service.sm.actions;

import guru.springframework.brewery.events.AllocateBeerOrderRequest;
import guru.springframework.beer.order.service.config.JMSConfig;
import guru.springframework.beer.order.service.domain.BeerOrder;
import guru.springframework.beer.order.service.domain.BeerOrderEventEnum;
import guru.springframework.beer.order.service.domain.BeerOrderStatusEnum;
import guru.springframework.beer.order.service.repositories.BeerOrderRepository;
import guru.springframework.beer.order.service.sm.config.BeerOrderStateMachineConfig;
import guru.springframework.beer.order.service.web.mappers.BeerOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class AllocateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
             String beerOrderId = (String) stateContext.getMessage().getHeaders().get(BeerOrderStateMachineConfig.BEER_ORDER_ID_HEADER);
             Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(UUID.fromString(beerOrderId));

             beerOrderOptional.ifPresentOrElse(beerOrder -> {
             jmsTemplate.convertAndSend(JMSConfig.ALLOCATE_ORDER_QUEUE,
                     AllocateBeerOrderRequest.builder().beerOrderDto(
                            beerOrderMapper.beerOrderToDto(beerOrder)).build());
             }, () -> log.debug("Beer Order not found"));

             log.debug("Sent Allocation Request for order id: " + beerOrderId);
    }
}
