package guru.springframework.beer.order.service.sm.actions;

import guru.springframework.beer.order.service.domain.BeerOrderEventEnum;
import guru.springframework.beer.order.service.domain.BeerOrderStatusEnum;
import guru.springframework.beer.order.service.sm.config.BeerOrderStateMachineConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AllocationFailureAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
        String beerOrderId = (String) stateContext.getMessageHeader(BeerOrderStateMachineConfig.BEER_ORDER_ID_HEADER);
        log.error("Allocation failed for order: " + beerOrderId);
    }
}
