package br.com.poc.rabbitmq.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsumerRegistry {

    private static ConsumerRegistry instance = null;

    private ConsumerRegistry() {

    }

    public static synchronized ConsumerRegistry getInstance() {
        if(null == instance) {
            instance = new ConsumerRegistry();
        }
        return instance;
    }

    private final List<QueueListener> listeners = new ArrayList<>();

    public void register(QueueListener listener) {
        listeners.add(listener);
    }

    public void initConsumers() throws IOException {
        for(QueueListener listener : listeners) {
            listener.start();
        }
    }
}
