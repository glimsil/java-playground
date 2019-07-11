package br.com.poc.rabbitmq;


import br.com.poc.rabbitmq.config.ConsumerRegistry;
import br.com.poc.rabbitmq.config.QueueListener;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.Set;

public class RabbitmqApplication {

    public static void main(String[] args) throws IOException {
        register();
        ConsumerRegistry.getInstance().initConsumers();
    }

    public static void register() { // as we're not using injection, lets do it mannually
        Reflections reflections = new Reflections("br.com.poc.rabbitmq.consumer");
        Set<Class<? extends QueueListener>> allClasses =
                reflections.getSubTypesOf(QueueListener.class);
        allClasses.forEach(listenerClass -> {
            try {
                ConsumerRegistry.getInstance().register(listenerClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

}
