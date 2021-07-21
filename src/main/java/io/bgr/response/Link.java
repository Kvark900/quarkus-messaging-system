package io.bgr.response;

import java.io.Serializable;

public class Link implements Serializable {
    private final String url;
    private final String rel;

    public Link(String url, String rel) {
        this.url = url;
        this.rel = rel;
    }

    public String getUrl() {
        return url;
    }

    public String getRel() {
        return rel;
    }
}
