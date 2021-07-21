package io.bgr.repository;

import io.bgr.model.Message;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MessagingRepository implements PanacheRepository<Message> {

    public List<Message> getMessagesBySenderUUID(UUID uuid) {
        String query = "SELECT m from Message m join fetch m.attachments where m.senderId = ?1";
        return find(query, uuid).list();
    }
}
