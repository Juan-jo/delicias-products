package org.delicias.product.dto;

import jakarta.validation.constraints.NotNull;
import org.delicias.common.validation.OnCreate;

public record CreateProductTmplDTO(

        @NotNull(message = "The parameter is mandatory", groups = { OnCreate.class})
        Integer restaurantTmplId,

        @NotNull(message = "The parameter is mandatory", groups = { OnCreate.class})
        String name,

        @NotNull(message = "The parameter is mandatory", groups = { OnCreate.class})
        String description
) { }
