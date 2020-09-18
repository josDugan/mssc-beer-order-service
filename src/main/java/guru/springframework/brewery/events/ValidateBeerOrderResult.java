package guru.springframework.brewery.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateBeerOrderResult implements Serializable {

    private static final long serialVersionUID = 8226074636970739337L;

    private UUID orderId;
    private Boolean isValid;
}
