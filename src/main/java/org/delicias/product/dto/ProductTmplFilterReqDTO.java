package org.delicias.product.dto;

import lombok.Getter;
import lombok.Setter;
import org.delicias.common.dto.BaseFilterDTO;

@Getter
@Setter
public class ProductTmplFilterReqDTO extends BaseFilterDTO {
    String name;
}
