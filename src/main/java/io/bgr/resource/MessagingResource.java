package io.bgr.resource;

import io.bgr.model.Message;
import io.bgr.repository.MessagingRepository;

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
    private MessagingRepository messagingRepository;

    @GET
    @Path("/init")
    @Transactional
    public Response initData() {
        getDummyData().forEach(msg -> messagingRepository.persist(msg));
        return ok("Init success!").build();
    }

    private List<Message> getDummyData() {
        return List.of(
                new Message("Hello"),
                new Message("Quarkus"),
                new Message("Spring Boot New - Hot deploy")
        );
    }

}
