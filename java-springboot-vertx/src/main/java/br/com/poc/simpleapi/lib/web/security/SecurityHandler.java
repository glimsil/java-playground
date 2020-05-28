package br.com.poc.simpleapi.lib.web.security;

import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SecurityHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void secure(RoutingContext routingContext, String[] permissions) {
        auth();
        logger.info("Passing security handler. Checked permissions " + Strings.join(Arrays.asList(permissions).iterator(), ','));
    }

    private void auth() {

    }

}
