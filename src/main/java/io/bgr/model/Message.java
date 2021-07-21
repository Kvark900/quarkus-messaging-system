package io.bgr.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class Message implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private UUID senderId;

    private UUID recipientId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    private String message;

    @OneToMany(mappedBy = "message",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<MessageAttachment> attachments;

    public Message() {
    }

    public Message(String message) {
        this.message = message;
    }

    public Message(String message, Set<MessageAttachment> attachments) {
        this.message = message;
        this.attachments = attachments;
    }

    public Long getId() {
        return id;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        this.recipientId = recipientId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<MessageAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<MessageAttachment> attachments) {
        this.attachments = attachments;
    }

}
