package guru.springframework.beer.order.service.services.testcomponents;

import guru.springframework.brewery.events.ValidateBeerOrderRequest;
import guru.springframework.brewery.events.ValidateBeerOrderResult;
import guru.springframework.beer.order.service.config.JMSConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidationListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JMSConfig.VALIDATE_ORDER_QUEUE)
    public void listen(Message msg) {
        ValidateBeerOrderRequest request = (ValidateBeerOrderRequest) msg.getPayload();
        boolean isValid = true;

        if ("fail-validation".equals(request.getBeerOrderDto().getCustomerRef())) {
            isValid = false;
        }

        log.debug("test component BeerOrderValidationListener ##################################");

        jmsTemplate.convertAndSend(JMSConfig.VALIDATE_ORDER_RESULT_QUEUE,
                ValidateBeerOrderResult.builder()
                        .isValid(isValid)
                        .orderId(request.getBeerOrderDto().getId())
                        .build());
    }
}
