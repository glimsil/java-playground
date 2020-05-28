package br.com.poc.simpleapi.lib.web.annotation;

import io.vertx.core.http.HttpMethod;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Endpoint {
  public HttpMethod method();
  public String path() default "";
}