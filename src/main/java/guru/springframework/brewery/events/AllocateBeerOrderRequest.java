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
public class AllocateBeerOrderRequest implements Serializable {

    private static final long serialVersionUID = 9133332213709946246L;

    private BeerOrderDto beerOrderDto;
}
