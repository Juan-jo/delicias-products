package org.delicias.attribute_value.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnUpdate;

import java.math.BigDecimal;


@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductAttributeValueDTO(

        @NotNull(message = "is mandatory", groups = { OnUpdate.class })
        Integer id,

        @NotNull(message = "is mandatory", groups = { OnCreate.class, OnUpdate.class })
        String name,

        @NotNull(message = "is mandatory", groups = { OnCreate.class, OnUpdate.class })
        BigDecimal extraPrice,

        @NotNull(message = "is mandatory", groups = { OnCreate.class, OnUpdate.class })
        Short sequence
) { }
