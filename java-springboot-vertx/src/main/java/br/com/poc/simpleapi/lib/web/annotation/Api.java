package br.com.poc.simpleapi.lib.web.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Api {
  public String value() default "";
}