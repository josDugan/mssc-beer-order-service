package guru.springframework.beer.order.service.sm.actions;

import guru.springframework.brewery.events.ValidateBeerOrderRequest;
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
public class ValidateOrderRequestAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
        log.info("ValidateOrderRequestAction was called");

        Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(UUID.fromString((String)stateContext.getMessageHeader(BeerOrderStateMachineConfig.BEER_ORDER_ID_HEADER)));

        beerOrderOptional.ifPresentOrElse(beerOrder -> {
            jmsTemplate.convertAndSend(JMSConfig.VALIDATE_ORDER_QUEUE, ValidateBeerOrderRequest.builder().beerOrderDto(beerOrderMapper.beerOrderToDto(beerOrder)).build());
        }, () -> log.debug("Beer Order not present"));
    }
}
