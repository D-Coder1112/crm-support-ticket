package com.sofzenix.crm.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sofzenix.crm.model.Ticket;
import com.sofzenix.crm.service.TicketService;

@Path("/tickets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketResource {

    @Inject
    TicketService service;

    // CREATE TICKET
    @POST
    public Ticket create(Ticket ticket) {
        return service.create(ticket);
    }

    // AUTO QUALIFY TICKET
    @PUT
    @Path("/{id}/qualify")
    public Ticket qualify(@PathParam("id") Long id) {
        return service.qualify(id);
    }

    // RESOLVE OR ESCALATE
    @PUT
    @Path("/{id}/resolve")
    public Ticket resolve(
            @PathParam("id") Long id,
            @QueryParam("solved") boolean solved) {

        return service.resolve(id, solved);
    }
}
