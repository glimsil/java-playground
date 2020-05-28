package br.com.poc.simpleapi.lib.web;

import br.com.poc.simpleapi.lib.utils.PackageScanUtils;
import br.com.poc.simpleapi.lib.web.annotation.DeclarePreHookHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PreHookHandlerConfig {
    private Map<String, PreHookHandler> map = new HashMap<>();

    public void configPreHooks(String apiPackage) {
        List<Class<?>> list = PackageScanUtils.getAnnotatedClassesInPackage(apiPackage, DeclarePreHookHandler.class);
        list.forEach(clazz -> {
            Object instance = null;
            try {
                instance = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if(instance instanceof PreHookHandler) {
                DeclarePreHookHandler declarePreHookHandler = clazz.getAnnotation(DeclarePreHookHandler.class);
                map.put(declarePreHookHandler.value(), (PreHookHandler) instance);
            }

        });
    }

    public Map<String, PreHookHandler> getMap() {
        return map;
    }
}
