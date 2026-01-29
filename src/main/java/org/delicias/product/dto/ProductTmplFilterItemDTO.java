package org.delicias.product.dto;

import lombok.Builder;

@Builder
public record ProductTmplFilterItemDTO(
        Integer id,
        String name,
        String picture
) {}
