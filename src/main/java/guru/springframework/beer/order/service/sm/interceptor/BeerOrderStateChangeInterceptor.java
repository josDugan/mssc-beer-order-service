package guru.springframework.beer.order.service.sm.interceptor;

import guru.springframework.beer.order.service.domain.BeerOrder;
import guru.springframework.beer.order.service.domain.BeerOrderEventEnum;
import guru.springframework.beer.order.service.domain.BeerOrderStatusEnum;
import guru.springframework.beer.order.service.repositories.BeerOrderRepository;
import guru.springframework.beer.order.service.sm.config.BeerOrderStateMachineConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderStateChangeInterceptor extends StateMachineInterceptorAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;

    @Override
    public void preStateChange(State<BeerOrderStatusEnum, BeerOrderEventEnum> state, Message<BeerOrderEventEnum> message, Transition<BeerOrderStatusEnum, BeerOrderEventEnum> transition, StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine) {
        Optional.ofNullable(message)
            .flatMap(msg -> Optional.ofNullable((String) msg.getHeaders().getOrDefault(BeerOrderStateMachineConfig.BEER_ORDER_ID_HEADER, "")))
            .ifPresent(
                    orderId -> {
                        log.debug("Saving state for order id: " + orderId + " Status: " + state.getId());

                        Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(UUID.fromString(orderId));

                        beerOrderOptional.ifPresentOrElse(beerOrder -> {
                            beerOrder.setOrderStatus(state.getId());

                            beerOrderRepository.saveAndFlush(beerOrder);
                        }, () -> log.debug("Beer Order not found for order id: " + orderId));
                    }
            );
    }
}
