package org.delicias.attribute.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.delicias.common.dto.product.DisplayAttrType;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnUpdate;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductAttributeDTO(

        @NotNull(message = "id is mandatory", groups = { OnUpdate.class })
        Integer id,

        @NotNull(message = "name is mandatory", groups = { OnCreate.class, OnUpdate.class })
        String name,

        @NotNull(message = "displayType is mandatory", groups = { OnCreate.class, OnUpdate.class})
        DisplayAttrType displayType,

        @NotNull(message = "sequence is mandatory", groups = { OnCreate.class, OnUpdate.class })
        Short sequence

) { }
