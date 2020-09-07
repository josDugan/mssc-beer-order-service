package guru.springframework.services.listeners;

import guru.springframework.common.events.ValidateBeerOrderResult;
import guru.springframework.config.JMSConfig;
import guru.springframework.services.BeerOrderManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ValidateBeerOrderResponseListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JMSConfig.VALIDATE_ORDER_RESULT_QUEUE)
    public void listen(ValidateBeerOrderResult validateBeerOrderResult) {
        log.debug("Received validate-order-result message.");

        beerOrderManager.processBeerOrderValidation(validateBeerOrderResult.getOrderId(), validateBeerOrderResult.getIsValid());
    }
}
