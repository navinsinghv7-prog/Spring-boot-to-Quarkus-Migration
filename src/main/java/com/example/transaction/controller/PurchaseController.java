package com.example.transaction.controller;

import com.example.transaction.service.PurchaseService;
import com.example.transaction.service.exception.InsufficientFundsException;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/purchase")
@Produces(MediaType.TEXT_PLAIN)
public class PurchaseController {

    @Inject
    PurchaseService purchaseService;

    @POST
    @Path("/{customerId}/{productId}")
    public Response purchaseProduct(@PathParam("customerId") Long customerId,
                                    @PathParam("productId") Long productId) {
        try {
            purchaseService.purchaseProduct(customerId, productId);
            return Response.ok("Purchase successful").build();
        } catch (InsufficientFundsException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity("An error occurred").build();
        }
    }
}
