package io.bgr.repository;

import io.bgr.model.MessageAttachment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AttachmentRepository implements PanacheRepository<MessageAttachment> {

}
