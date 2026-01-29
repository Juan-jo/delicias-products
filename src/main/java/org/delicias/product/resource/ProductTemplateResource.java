package org.delicias.product.resource;


import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnFilter;
import org.delicias.common.validation.OnUpdate;
import org.delicias.product.dto.CreateProductTmplDTO;
import org.delicias.product.dto.ProductTmplDTO;
import org.delicias.product.dto.ProductTmplFilterReqDTO;
import org.delicias.product.service.ProductTemplateService;

@Authenticated
@Path("/api/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductTemplateResource {

    @Inject
    ProductTemplateService service;

    @POST
    public Response create(
            @Valid @ConvertGroup(to = OnCreate.class) CreateProductTmplDTO req
    ) {
        var response = service.create(req);

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @PUT
    public Response update(
            @Valid @ConvertGroup(to = OnUpdate.class) ProductTmplDTO req
    ) {
        service.update(req);

        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    public Response findById(
            @PathParam("id") Integer productTmplId
    ) {
        var response = service.findById(productTmplId);

        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(
            @PathParam("id") Integer productTmplId
    ) {
        service.deleteById(productTmplId);

        return Response.noContent().build();
    }

    @POST
    @Path("/filter")
    public Response filter(
            @Valid @ConvertGroup(to = OnFilter.class) ProductTmplFilterReqDTO req
    ) {
        var filtered = service.filterSearch(req);

        return Response.ok(filtered).build();
    }
}
