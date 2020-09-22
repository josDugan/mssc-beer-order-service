package guru.springframework.brewery.events;

import guru.springframework.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeallocateOrderRequest implements Serializable {

    private static final long serialVersionUID = -1420705941747553228L;

    private BeerOrderDto beerOrderDto;
}
