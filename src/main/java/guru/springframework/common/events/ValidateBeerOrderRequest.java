package guru.springframework.common.events;

import guru.springframework.common.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateBeerOrderRequest implements Serializable {

    private static final long serialVersionUID = 5459749023251721901L;

    private BeerOrderDto beerOrderDto;

}
