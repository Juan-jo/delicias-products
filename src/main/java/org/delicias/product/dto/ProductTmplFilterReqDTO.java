package org.delicias.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.delicias.common.dto.BaseFilterDTO;
import org.delicias.common.validation.OnFilter;

@Getter
@Setter
public class ProductTmplFilterReqDTO extends BaseFilterDTO {
    String name;

    @NotNull(message = "Is mandatory.", groups = { OnFilter.class })
    Integer restaurantTmplId;
}
