package org.delicias.attribute.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.attribute.dto.ProductAttributeDTO;
import org.delicias.attribute.service.ProductAttributeService;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnUpdate;

@Authenticated
@Path("/api/products/{productTmplId}/attributes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductAttributeResource {

    @Inject
    ProductAttributeService service;

    @GET
    public Response findByProduct(
            @PathParam("productTmplId") Integer productTmplId) {

        return Response.ok(
                service.findByProduct(productTmplId)
        ).build();
    }

    @GET
    @Path("/{attributeId}")
    public Response findById(
            @PathParam("attributeId") Integer attributeId) {

        return Response.ok(
                service.findById(attributeId)
        ).build();
    }

    @POST
    public Response create(
            @PathParam("productTmplId") Integer productTmplId,
            @Valid @ConvertGroup(to = OnCreate.class) ProductAttributeDTO req
    ) {

        service.create(productTmplId, req);

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    public Response update(
            @Valid @ConvertGroup(to = OnUpdate.class) ProductAttributeDTO req
    ) {

        service.update(req);

        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(
            @PathParam("id") Integer productAttrId
    ) {
        service.deleteById(productAttrId);

        return Response.noContent().build();
    }

}
