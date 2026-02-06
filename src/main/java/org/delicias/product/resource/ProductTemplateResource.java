package org.delicias.product.resource;


import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.common.dto.RestaurantMenuProductDTO;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnFilter;
import org.delicias.common.validation.OnUpdate;
import org.delicias.product.dto.CreateProductTmplDTO;
import org.delicias.product.dto.ProductTmplDTO;
import org.delicias.product.dto.ProductTmplFilterReqDTO;
import org.delicias.product.service.ProductTemplateService;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Authenticated
@Path("/api/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductTemplateResource {

    @Inject
    ProductTemplateService service;

    @GET
    @Path("/batch")
    public List<RestaurantMenuProductDTO> getBatch(@QueryParam("ids") List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        return service.findByIds(ids);
    }

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

    @PATCH
    @Path("/{productTmplId}")
    public Response patch(
            @PathParam("productTmplId") Integer id,
            Map<String, Object> data
    ) {

        service.patch(id, data);
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


    @PUT
    @Path("/picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateLogo(
            @NotNull @FormParam("productTmplId") Integer productTmplId,
            @NotNull @FormParam("file") FileUpload file
    ) throws IOException {

        Map<String, String> response = service.uploadPicture(productTmplId, file);

        return Response.ok(response).build();
    }
}
