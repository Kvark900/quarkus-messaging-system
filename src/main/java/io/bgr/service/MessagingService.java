package io.bgr.service;


import io.bgr.model.Message;
import io.bgr.model.MessageAttachment;
import io.bgr.repository.MessagingRepository;
import io.bgr.resource.MessagingResource;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.getProperty;

@ApplicationScoped
public class MessagingService {
    private final String FILE_FOLDER = getProperty("user.dir").replace("target", "src\\main\\resources\\attachment");
    private static final Logger log = Logger.getLogger(MessagingService.class);

    @Inject
    private MessagingRepository messagingRepository;


    public Set<String> saveAttachments(List<InputPart> files) throws IOException {
        Set<String> savedFilePaths = new HashSet<>();
        for (InputPart inputPart : files) {
            MultivaluedMap<String, String> header = inputPart.getHeaders();
            String fileName = new File(FILE_FOLDER) + File.separator + getFileName(header);
            saveFile(inputPart, fileName);
            savedFilePaths.add(fileName);
        }
        return savedFilePaths;
    }

    @Transactional
    public Optional<Message> getMessagesById(Long id, UriInfo uriInfo) {
        Optional<Message> message = messagingRepository.findByIdOptional(id);
        return getMessageHATEOAS(message, uriInfo);
    }

    @Transactional
    public List<Message> getMessagesBySenderUUID(String uuid, UriInfo uriInfo) {
        return messagingRepository.getMessagesBySenderUUID(UUID.fromString(uuid))
                .stream().map(message -> getMessageHATEOAS(Optional.of(message), uriInfo).get())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Message> getMessagesHATEOAS(UriInfo uriInfo) {
        List<Message> msgs = getAllMessagesAttachmentsIncluded();
        msgs.forEach(msg -> getMessageHATEOAS(Optional.of(msg), uriInfo));
        return msgs;
    }

    @Transactional
    public List<Message> getAllMessagesAttachmentsIncluded() {
        return messagingRepository.getAllMessagesAttachmentsIncluded();
    }

    @Transactional
    public void persistMessage(Message message) {
        log.debug("Saving message...");
        messagingRepository.persist(message);
        log.debug("Message saved");
    }

    public void saveFile(InputPart inputPart, String fileName) throws IOException {
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        byte[] bytes = IOUtils.toByteArray(inputStream);
        Files.write(Paths.get(fileName), bytes, StandardOpenOption.CREATE_NEW);
    }

    public String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if (filename.trim().startsWith("filename")) {
                String[] name = filename.split("=");
                return name[1].trim().replaceAll("\"", "");
            }
        }
        return "unknown";
    }

    @Transactional
    public void initDummyData() {
        getDummyData().forEach(msg -> messagingRepository.persist(msg));
    }

    private Optional<Message> getMessageHATEOAS(Optional<Message> message, UriInfo uriInfo) {
        if (message.isEmpty()) return message;
        Message msg = message.get();
        msg.addLink(getSelfLink(msg, uriInfo), "self");
        addAttachmentLinks(uriInfo, msg);
        return message;
    }

    private void addAttachmentLinks(UriInfo uriInfo, Message msg) {
        msg.getAttachments().forEach(attachment -> msg.addLink(getAttachmentLink(attachment, uriInfo), "attachments"));
    }

    private String getAttachmentLink(MessageAttachment attachment, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder()
                .path(MessagingResource.class)
                .path("/attachment")
                .path(String.valueOf(attachment.getId()))
                .build()
                .toString();
    }

    private String getSelfLink(Message msg, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder()
                .path(MessagingResource.class)
                .path(String.valueOf(msg.getId()))
                .build()
                .toString();
    }

    private List<Message> getDummyData() {
        return List.of(
                new Message("Hello"),
                new Message("Quarkus"),
                new Message("Spring Boot")
        );
    }
}
