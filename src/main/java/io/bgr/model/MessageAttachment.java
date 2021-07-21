package io.bgr.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;

@Entity
@Table(name = "message_attachment")
public class MessageAttachment implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "file_location")
    private String fileLocation;

    @Transient
    private File file;

    @JsonBackReference
    @ManyToOne
    private Message message;

    public MessageAttachment() {
    }

    public MessageAttachment(String fileLocation, Message message) {
        this.fileLocation = fileLocation;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
