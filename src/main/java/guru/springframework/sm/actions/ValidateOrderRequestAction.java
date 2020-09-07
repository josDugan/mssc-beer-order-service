package guru.springframework.sm.actions;

import guru.springframework.common.events.ValidateBeerOrderRequest;
import guru.springframework.config.JMSConfig;
import guru.springframework.domain.BeerOrder;
import guru.springframework.domain.BeerOrderEventEnum;
import guru.springframework.domain.BeerOrderStatusEnum;
import guru.springframework.repositories.BeerOrderRepository;
import guru.springframework.sm.config.BeerOrderStateMachineConfig;
import guru.springframework.web.mappers.BeerOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

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

        BeerOrder beerOrder = beerOrderRepository.findOneById(UUID.fromString((String)stateContext.getMessageHeader(BeerOrderStateMachineConfig.BEER_ORDER_ID_HEADER)));

        jmsTemplate.convertAndSend(JMSConfig.VALIDATE_ORDER_QUEUE, ValidateBeerOrderRequest.builder().beerOrderDto(beerOrderMapper.beerOrderToDto(beerOrder)).build());
    }
}
