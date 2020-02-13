package com.tr.bluemoon.employeeportal.client;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.util.Map;

public abstract class RestClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Client client = ClientBuilder.newClient();

    class AuthMethod {
        AuthMethod() {

        }
    }

    public class RestRequest {
        private Invocation.Builder invocationBuilder;
        private boolean retry = true;
        private boolean fallback = false;
        private MediaType responseType;

        private RestRequest() {

        }

        private RestRequest(String uri) {
            invocationBuilder = client.target(uri).request();
        }

        private RestRequest uri(String uri) {
            invocationBuilder = client.target(uri).request();
            return this;
        }

        public RestRequest responseType(MediaType type) {
            if(null == invocationBuilder) {
                throw new UnsupportedOperationException("URI needed.");
            }
            responseType = type;
            return this;
        }

        public RestRequest requestType(MediaType type) {
            if(null == invocationBuilder) {
                throw new UnsupportedOperationException("URI needed.");
            }
            invocationBuilder.accept(type);
            return this;
        }

        public RestRequest header(String name, String value) {
            if(null == invocationBuilder) {
                throw new UnsupportedOperationException("URI needed.");
            }
            invocationBuilder.header(name, value);
            return this;
        }

        public RestRequest headers(MultivaluedMap<String, Object> headers) {
            if(null == invocationBuilder) {
                throw new UnsupportedOperationException("URI needed.");
            }
            invocationBuilder.headers(headers);
            return this;
        }

        // Default true
        public RestRequest retry(boolean active) {
            this.retry = active;
            return this;
        }

        // Default false
        public RestRequest fallback(boolean active) {
            this.fallback = active;
            return this;
        }

        public Object get() {
            Invocation invocation = invocationBuilder.buildGet();
            if (retry) {
                return Failsafe.with(retryPolicy()).get(() -> invocation.invoke()).getEntity();
            }
            return invocation.invoke().getEntity();
        }

        public Object post(Object object) {
            Invocation invocation = invocationBuilder.buildPost(Entity.entity(object, responseType == null ?
                        MediaType.APPLICATION_JSON : responseType.getType()));
            if(retry) {
                return Failsafe.with(retryPolicy()).get(() -> invocation.invoke()).getEntity();
            }
            return invocation.invoke().getEntity();
        }

        public Object put(Object object) {
            Invocation invocation = invocationBuilder.buildPut(Entity.entity(object, responseType == null ?
                    MediaType.APPLICATION_JSON : responseType.getType()));
            if(retry) {
                return Failsafe.with(retryPolicy()).get(() -> invocation.invoke()).getEntity();
            }
            return invocation.invoke().getEntity();
        }

        public Object delete() {
            Invocation invocation = invocationBuilder.buildDelete();
            if(retry) {
                return Failsafe.with(retryPolicy()).get(() -> invocation.invoke()).getEntity();
            }
            return invocation.invoke().getEntity();
        }

    }

    protected RestRequest start() {
        return new RestRequest();
    }

    protected RestRequest start(String uri) {
        return new RestRequest();
    }

    private RetryPolicy retryPolicy() {
        return new RetryPolicy<>()
                .handle(Exception.class)
                .withDelay(Duration.ofMillis(1500))
                .onFailedAttempt(error -> logger.error(String.format("Failed attempt on http call to recipient %s. Error: %s", error.getLastResult())));
    }

    private Object fallbackMethod() {
        logger.info("Fallback method not implemented.");
        return null;
    }

}
