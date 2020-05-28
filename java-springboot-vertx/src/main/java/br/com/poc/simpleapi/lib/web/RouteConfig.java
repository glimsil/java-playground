package br.com.poc.simpleapi.lib.web;

import br.com.poc.simpleapi.lib.utils.PackageScanUtils;
import br.com.poc.simpleapi.lib.web.annotation.*;
import br.com.poc.simpleapi.lib.web.security.SecurityHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

@Configuration
public class RouteConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SecurityHandler securityHandler;

    public Router configRoutes(Vertx vertx, String apiPackage) {
        List<Class<?>> classes = PackageScanUtils.getAnnotatedClassesInPackage(apiPackage, Api.class);
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        for (Class<?> c : classes) {
            Api api = c.getAnnotation(Api.class);

            final Object instance = applicationContext.getBean(c);

            for (Method method : c.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Endpoint.class)) {
                    Endpoint endpoint = method.getAnnotation(Endpoint.class);
                    Secure secure = method.getAnnotation(Secure.class);
                    PreHook[] preHooks = method.getDeclaredAnnotationsByType(PreHook.class);
                    router.route().method(endpoint.method()).path(getFullPath(api.value(), endpoint.path())).
                            handler(r -> {
                                if(null != secure) {
                                    securityHandler.secure(r, secure.value());
                                }
                                try {
                                    method.invoke(instance, r);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            });
                }
            }

        }
        return router;
    }

    public void configPreHooks(String apiPackage) {

    }



    private void preHook(RoutingContext routingContext) {

    }

    private String getFullPath(String pathA, String pathB) {
        StringBuilder completePath = new StringBuilder();
        completePath.append("/");
        for (String subPath : pathA.split("/")) {
            if (!subPath.equals("")) {
                completePath.append(subPath);
                completePath.append("/");
            }
        }
        for (String subPath : pathB.split("/")) {
            if (!subPath.equals("")) {
                completePath.append(subPath);
                completePath.append("/");
            }
        }
        return completePath.toString();
    }
}
