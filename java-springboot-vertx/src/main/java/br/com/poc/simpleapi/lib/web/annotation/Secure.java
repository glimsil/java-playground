package br.com.poc.simpleapi.lib.web.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Secure {
  public String[] value() default {};
}