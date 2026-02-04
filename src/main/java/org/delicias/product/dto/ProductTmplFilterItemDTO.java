package org.delicias.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ProductTmplFilterItemDTO(
        Integer id,
        String name,
        String description,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
        BigDecimal listPrice,
        String picture
) {}
