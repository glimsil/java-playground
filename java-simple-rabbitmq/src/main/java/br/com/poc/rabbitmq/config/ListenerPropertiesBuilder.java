package br.com.poc.rabbitmq.config;

public final class ListenerPropertiesBuilder {
    private String queue;
    private String exchange;
    private String routingKey;

    private ListenerPropertiesBuilder() {
    }

    public static ListenerPropertiesBuilder create() {
        return new ListenerPropertiesBuilder();
    }

    public ListenerPropertiesBuilder queue(String queue) {
        this.queue = queue;
        return this;
    }

    public ListenerPropertiesBuilder exchange(String exchange) {
        this.exchange = exchange;
        return this;
    }

    public ListenerPropertiesBuilder routingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public ListenerProperties build() {
        ListenerProperties listenerProperties = new ListenerProperties();
        listenerProperties.setQueue(queue);
        listenerProperties.setExchange(exchange);
        listenerProperties.setRoutingKey(routingKey);
        return listenerProperties;
    }
}

