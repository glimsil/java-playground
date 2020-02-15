package com.glimsil.poc.elasticsearch;

import com.glimsil.poc.elasticsearch.client.ElasticSearchClientProvider;
import com.glimsil.poc.elasticsearch.model.Document;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException {
        ElasticSearchClientProvider elasticSearchClientProvider = new ElasticSearchClientProvider();
        Random random = new Random();
        System.out.println("Insertion started.");
        for (int i = 0; i < 1000; i++) {
            String documentUri = i + "-" + UUID.randomUUID().toString() + ".com.br";
            String basis = random.nextInt(30) + "/" + random.nextInt(12);
            Document document = new Document();
            document.setDocumentLink(i + "-" + UUID.randomUUID().toString() + ".com.br");
            document.setDocumentName("Name " + i);
            elasticSearchClientProvider.indexDocument(document);
        }
        System.out.println("Insertion ended.");
        List<Document> documents = elasticSearchClientProvider.phraseFullTextSearch("Name 92", Document.class.getSimpleName().toLowerCase());
        documents.forEach(document -> System.out.println("{\"documentName\": \"" + document.getDocumentName()
                + "\", \"documentLink\": \"" + document.getDocumentLink() + "\"}"));
        elasticSearchClientProvider.close();
    }

}
