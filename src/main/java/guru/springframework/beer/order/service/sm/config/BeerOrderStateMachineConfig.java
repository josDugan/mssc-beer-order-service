package guru.springframework.beer.order.service.sm.config;

import guru.springframework.beer.order.service.domain.BeerOrderEventEnum;
import guru.springframework.beer.order.service.domain.BeerOrderStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@RequiredArgsConstructor
@Configuration
@EnableStateMachineFactory
public class BeerOrderStateMachineConfig extends StateMachineConfigurerAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {

    public static final String BEER_ORDER_ID_HEADER = "BEER_ORDER_ID_HEADER";

    private final Action<BeerOrderStatusEnum, BeerOrderEventEnum> validateOrderRequestAction;
    private final Action<BeerOrderStatusEnum, BeerOrderEventEnum> allocateOrderAction;
    private final Action<BeerOrderStatusEnum, BeerOrderEventEnum> validationFailureAction;
    private final Action<BeerOrderStatusEnum, BeerOrderEventEnum> allocationFailureAction;
    private final Action<BeerOrderStatusEnum, BeerOrderEventEnum> partialAllocationAction;
    private final Action<BeerOrderStatusEnum, BeerOrderEventEnum> deallocateOrderAction;
    private final Action<BeerOrderStatusEnum, BeerOrderEventEnum> dispatchOrderAction;

    @Override
    public void configure(StateMachineStateConfigurer<BeerOrderStatusEnum, BeerOrderEventEnum> states) throws Exception {
        states.withStates()
                .initial(BeerOrderStatusEnum.NEW)
                .states(EnumSet.allOf(BeerOrderStatusEnum.class))
                .end(BeerOrderStatusEnum.PICKED_UP)
                .end(BeerOrderStatusEnum.DELIVERED)
                .end(BeerOrderStatusEnum.DELIVERY_EXCEPTION)
                .end(BeerOrderStatusEnum.VALIDATION_EXCEPTION)
                .end(BeerOrderStatusEnum.ALLOCATION_EXCEPTION)
                .end(BeerOrderStatusEnum.CANCELED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<BeerOrderStatusEnum, BeerOrderEventEnum> transitions) throws Exception {
        transitions
                .withExternal()
                    .source(BeerOrderStatusEnum.NEW).target(BeerOrderStatusEnum.VALIDATION_PENDING)
                    .event(BeerOrderEventEnum.VALIDATE_ORDER)
                    .action(validateOrderRequestAction)
                .and().withExternal()
                    .source(BeerOrderStatusEnum.VALIDATION_PENDING).target(BeerOrderStatusEnum.VALIDATED)
                    .event(BeerOrderEventEnum.VALIDATION_PASSED)
                .and().withExternal()
                    .source(BeerOrderStatusEnum.VALIDATION_PENDING).target(BeerOrderStatusEnum.CANCELED)
                    .event(BeerOrderEventEnum.CANCEL_ORDER)
                .and().withExternal()
                    .source(BeerOrderStatusEnum.VALIDATION_PENDING).target(BeerOrderStatusEnum.VALIDATION_EXCEPTION)
                    .event(BeerOrderEventEnum.VALIDATION_FAILED)
                    .action(validationFailureAction)
                .and().withExternal()
                    .source(BeerOrderStatusEnum.VALIDATED).target(BeerOrderStatusEnum.ALLOCATION_PENDING)
                    .event(BeerOrderEventEnum.ALLOCATE_ORDER)
                    .action(allocateOrderAction)
                .and().withExternal()
                    .source(BeerOrderStatusEnum.VALIDATED).target(BeerOrderStatusEnum.CANCELED)
                    .event(BeerOrderEventEnum.CANCEL_ORDER)
                .and().withExternal()
                    .source(BeerOrderStatusEnum.ALLOCATION_PENDING).target(BeerOrderStatusEnum.ALLOCATED)
                    .event(BeerOrderEventEnum.ALLOCATION_SUCCESS)
                .and().withExternal()
                    .source(BeerOrderStatusEnum.ALLOCATION_PENDING).target(BeerOrderStatusEnum.CANCELED)
                    .event(BeerOrderEventEnum.CANCEL_ORDER)
                .and().withExternal()
                    .source(BeerOrderStatusEnum.ALLOCATION_PENDING).target(BeerOrderStatusEnum.ALLOCATION_EXCEPTION)
                    .event(BeerOrderEventEnum.ALLOCATION_FAILED)
                    .action(allocationFailureAction)
                .and().withExternal()
                    .source(BeerOrderStatusEnum.ALLOCATION_PENDING).target(BeerOrderStatusEnum.PENDING_INVENTORY)
                    .event(BeerOrderEventEnum.ALLOCATION_NO_INVENTORY)
                    .action(partialAllocationAction)
                .and().withExternal()
                    .source(BeerOrderStatusEnum.ALLOCATED).target(BeerOrderStatusEnum.PICKED_UP)
                    .event(BeerOrderEventEnum.BEERORDER_PICKED_UP)
                .and().withExternal()
                    .source(BeerOrderStatusEnum.ALLOCATED).target(BeerOrderStatusEnum.CANCELED)
                    .event(BeerOrderEventEnum.CANCEL_ORDER)
                    .action(deallocateOrderAction);
    }

}
