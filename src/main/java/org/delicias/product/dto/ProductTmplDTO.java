package org.delicias.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.delicias.common.validation.OnUpdate;

import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductTmplDTO(
        @NotNull(message = "The parameter is mandatory", groups = { OnUpdate.class})
        Integer id,

        Integer restaurantTmplId,

        @NotNull(message = "The parameter is mandatory", groups = { OnUpdate.class})
        String name,

        @NotNull(message = "The parameter is mandatory", groups = { OnUpdate.class})
        String description,

        @NotNull(message = "The parameter is mandatory", groups = { OnUpdate.class})
        BigDecimal listPrice,

        String picture,

        boolean salesOk
) { }
