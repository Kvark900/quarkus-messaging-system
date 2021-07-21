package io.bgr.service;


import io.bgr.model.Message;
import io.bgr.repository.MessagingRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MessagingService {
    private static final Logger log = Logger.getLogger(MessagingService.class);

    @Inject
    private MessagingRepository messagingRepository;

    @Transactional
    public Optional<Message> getMessagesById(Long id, UriInfo uriInfo) {
        return messagingRepository.findByIdOptional(id);
    }

    @Transactional
    public List<Message> getMessagesBySenderUUID(String uuid, UriInfo uriInfo) {
        return messagingRepository.getMessagesBySenderUUID(UUID.fromString(uuid));
    }


    @Transactional
    public void persistMessage(Message message) {
        log.debug("Saving message...");
        messagingRepository.persist(message);
        log.debug("Message saved");
    }

    @Transactional
    public void initDummyData() {
        getDummyData().forEach(msg -> messagingRepository.persist(msg));
    }

    private List<Message> getDummyData() {
        return List.of(
                new Message("Hello"),
                new Message("Quarkus"),
                new Message("Spring Boot")
        );
    }
}
