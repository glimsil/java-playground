package com.glimsil.poc.elasticsearch.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glimsil.poc.elasticsearch.exception.BodyParsingException;
import com.glimsil.poc.elasticsearch.exception.FullTextSearchException;
import com.glimsil.poc.elasticsearch.exception.IndexingFailedException;
import com.glimsil.poc.elasticsearch.model.Document;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElasticSearchClientProvider {

    private ObjectMapper objectMapper = new ObjectMapper().
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final Integer DEFAULT_SIZE = 10000;

    private final RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")
            )
        );

    public RestHighLevelClient getClient() {
        return client;
    }

    public Object indexDocument(Object document) {
        byte[] documentBytes;
        try {
            documentBytes = objectMapper.writeValueAsString(document).getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BodyParsingException("Failed to parse body", e);
        }
        IndexRequest indexRequest = new IndexRequest()
                .source(documentBytes, XContentType.JSON)
                .index(document.getClass().getSimpleName().toLowerCase());
        try {
            return client.index(indexRequest, RequestOptions.DEFAULT).toString();
        } catch (Exception e) {
            System.out.println("Error while indexing document: " + e.getMessage());
            throw new IndexingFailedException("Error while indexing document: " + e.getMessage(), e);
        }
    }

    public List<Document> wordFullTextSearch(String word, String... index) {
        QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder("*" + word + "*")
                .defaultField("*");
        return executeSearch(queryStringQueryBuilder, index);
    }

    public List<Document> phrasePrefixSearchByField(String phrase, String... index) {
        MatchPhrasePrefixQueryBuilder matchPhrasePrefixQueryBuilder = new MatchPhrasePrefixQueryBuilder("documentName", phrase);
        return executeSearch(matchPhrasePrefixQueryBuilder, index);
    }

    public List<Document> phraseFullTextSearch(String phrase, String... index) {
        // Will query by prefixes of the text. Separators, like space - , . special characters count like a new start.
        // so if i want to query c2-ab of a phrase aaac2-ab it will not work
        MultiMatchQueryBuilder multiMatchQueryBuilder = new MultiMatchQueryBuilder(phrase, "documentName", "documentLink").type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX);
        return executeSearch(multiMatchQueryBuilder, index);
    }

    public void deleteDocumentByMatchingField(String index, String field, String value) {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest()
                .setQuery(new MatchQueryBuilder(field, value));
        deleteByQueryRequest.indices(index);
        try {
            client.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            System.out.println("Error while deleting document: " + e.getMessage());
        }
    }

    private List<Document> executeSearch(AbstractQueryBuilder abstractQueryBuilder, String... index) {
        SearchRequest searchRequest = new SearchRequest();
        if (index != null) {
            searchRequest.indices(index);
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(abstractQueryBuilder);
        searchSourceBuilder.size(DEFAULT_SIZE);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            List<Document> documents = new ArrayList<>(response.getHits().getHits().length);
            for(SearchHit searchHit : response.getHits().getHits()) {
                documents.add(objectMapper.readValue(searchHit.getSourceAsString(), Document.class));
            }
            return documents;
        } catch (Exception e) {
            System.out.println("Error while executing full text search: " + e.getMessage());
            throw new FullTextSearchException("Error while executing full text search: " + e.getMessage(), e);
        }
    }

    public void close() throws IOException {
        client.close();
    }
}
