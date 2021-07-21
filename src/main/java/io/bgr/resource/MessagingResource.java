package io.bgr.resource;

import io.bgr.model.Message;
import io.bgr.repository.MessagingRepository;
import io.bgr.service.MessagingService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.ok;

@Path("/message")
public class MessagingResource {

    @Inject
    private MessagingService messagingService;

    @GET
    @Path("/init")
    @Transactional
    public Response initData() {
        messagingService.initDummyData();
        return ok("Init success!").build();
    }

}
