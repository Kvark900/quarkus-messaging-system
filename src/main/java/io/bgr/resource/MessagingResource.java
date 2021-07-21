package io.bgr.resource;

import io.bgr.model.Message;
import io.bgr.model.MessageAttachment;
import io.bgr.service.MessagingService;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.*;

@Path("/message")
public class MessagingResource {
    public static final String FILE_FORM_FIELD = "file";
    public static final String MESSAGE_FORM_FIELD = "message";
    private static final Logger log = Logger.getLogger(MessagingResource.class);

    @Inject
    private MessagingService messagingService;

    @GET
    @Path("/init")
    @Transactional
    public Response initData() {
        messagingService.initDummyData();
        return ok("Init success!").build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response postMessage(@MultipartForm MultipartFormDataInput input) {
        try {
            Map<String, List<InputPart>> form = input.getFormDataMap();
            List<InputPart> messageList = form.get(MESSAGE_FORM_FIELD);
            List<InputPart> files = form.get(FILE_FORM_FIELD);

            if (messageList == null || messageList.isEmpty()) throw new IllegalArgumentException("Message not found!");

            Message message = new Message(messageList.get(0).getBody(String.class, null), new HashSet<>());

            Set<String> strings = messagingService.saveAttachments(files);
            strings.forEach(s -> message.getAttachments().add(new MessageAttachment(s, message)));
            messagingService.persistMessage(message);
        }
        catch (Exception e) {
            log.error(e.toString());
            return Response.status(INTERNAL_SERVER_ERROR.getStatusCode()).entity("Internal server error:\n" + e.toString()).build();
        }
        return ok("Message upload success").build();
    }

    @GET
    @Path("/sender/{uuid}")
    @Transactional
    public Response getMessageBySenderUUID(@PathParam String uuid, @Context UriInfo uriInfo) {
        List<Message> msgs = messagingService.getMessagesBySenderUUID(uuid, uriInfo);
        return msgs != null && !msgs.isEmpty() ?
                ok(msgs).build() :
                noContent().build();
    }

    @GET
    @Path("/{id}")
    public Response getMessagesById(@PathParam Long id, @Context UriInfo uriInfo) {
        return messagingService
                .getMessagesById(id, uriInfo)
                .map(message -> ok(message).build())
                .orElse(status(NOT_FOUND).build());
    }
}
