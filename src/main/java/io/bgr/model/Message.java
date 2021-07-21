package io.bgr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.bgr.response.Link;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class Message implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "sender_id")
    private UUID senderId;

    @Column(name = "recipient_id")
    private UUID recipientId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    private String message;

    @JsonIgnore
    @OneToMany(mappedBy = "message",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<MessageAttachment> attachments;

    @Transient
    List<Link> links = new ArrayList<>();

    public Message() {
    }

    public Message(String message) {
        this.message = message;
    }

    public Message(String message, Set<MessageAttachment> attachments) {
        this.message = message;
        this.attachments = attachments;
    }

    @PrePersist
    protected void onCreate() {
        senderId = UUID.randomUUID();
        recipientId = UUID.randomUUID();
    }

    public void addLink(String url, String rel) {
        Link link = new Link(url, rel);
        links.add(link);
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

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
