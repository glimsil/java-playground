package com.glimsil.poc.elasticsearch.model;

public class Document {
    private String documentLink;
    private String documentName;

    public String getDocumentLink() {
        return documentLink;
    }

    public void setDocumentLink(String documentLink) {
        this.documentLink = documentLink;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @Override
    public String toString() {
        return "{" +
                "\"documentLink\": \"" + documentLink + "\"" +
                ", \"documentName\": \"" + documentName + "\"" +
                '}';
    }
}
