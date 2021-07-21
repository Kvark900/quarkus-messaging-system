package io.bgr.model;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;

@Entity
public class MessageAttachment implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String fileLocation;

    @Transient
    private File file;

    @ManyToOne
    private Message message;

    public MessageAttachment() {
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
