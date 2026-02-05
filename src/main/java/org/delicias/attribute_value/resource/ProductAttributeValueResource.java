package org.delicias.attribute_value.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.attribute_value.dto.ProductAttributeValueDTO;
import org.delicias.attribute_value.service.ProductAttributeValueService;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnUpdate;

@Authenticated
@Path("/api/products/{productTmplId}/attributes/{attributeId}/values")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductAttributeValueResource {

    @Inject
    ProductAttributeValueService service;

    @GET
    public Response findByAttribute(
            @PathParam("attributeId") Integer attributeId) {

        return Response.ok(
                service.findByAttribute(attributeId)
        ).build();
    }

    @POST
    public Response create(
            @PathParam("attributeId") Integer attributeId,
            @Valid @ConvertGroup(to = OnCreate.class) ProductAttributeValueDTO req
    ) {

        service.create(attributeId, req);
        return Response.status(Response.Status.CREATED).build();
    }


    @PUT
    public Response update(
            @PathParam("attributeId") Integer attributeId,
            @Valid @ConvertGroup(to = OnUpdate.class) ProductAttributeValueDTO req
    ) {

        service.update(req);

        return Response.status(Response.Status.OK).build();

    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(
            @PathParam("id") Integer attrValueId
    ) {
        service.deleteById(attrValueId);

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    public Response getById(
            @PathParam("id") Integer attrValueId
    ) {
        var response = service.findById(attrValueId);

        return Response.ok(
                response
        ).build();
    }


}
