package br.com.poc.simpleapi.lib.web.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PreHooks.class)
public @interface PreHook {
  public String name();
  public String[] params();
}