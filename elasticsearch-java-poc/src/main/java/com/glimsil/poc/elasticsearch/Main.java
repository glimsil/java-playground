package com.glimsil.poc.elasticsearch;

import com.glimsil.poc.elasticsearch.client.ElasticSearchClientProvider;
import com.glimsil.poc.elasticsearch.model.Document;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException {
        ElasticSearchClientProvider elasticSearchClientProvider = new ElasticSearchClientProvider();
        System.out.println("Insertion started.");
        /*for (int i = 0; i < 1000; i++) {
            Document document = new Document();
            document.setDocumentLink(i + "-" + UUID.randomUUID().toString() + ".com.br");
            document.setDocumentName("Name " + i);
            elasticSearchClientProvider.indexDocument(document);
        }*/
        System.out.println("Insertion ended.");
        Scanner scanner = new Scanner(System.in);
        String op;
        while(true) {
            System.out.println("Digite uma string para busca (0 para sair): ");
            op = scanner.nextLine();
            if("0".equals(op)){
                break;
            }

            List<Document> documents = elasticSearchClientProvider.phraseFullTextSearch(op, Document.class.getSimpleName().toLowerCase());
            documents.forEach(document -> System.out.println(document));
        }
        elasticSearchClientProvider.close();
    }

}
