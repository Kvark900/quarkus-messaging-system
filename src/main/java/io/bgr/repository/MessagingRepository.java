package io.bgr.repository;

import io.bgr.model.Message;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MessagingRepository implements PanacheRepository<Message> {
}
