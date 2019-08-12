package br.com.poc.zeromq;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.concurrent.CompletableFuture;

public class ZeroMqApplication {

    public static void main(String[] args) {
        consumer();
        publisher();
    }

    public static void consumer() {
        CompletableFuture.runAsync(() -> {
            try (ZContext context = new ZContext()) {
                ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
                subscriber.connect("tcp://localhost:5563");
                subscriber.subscribe("topic.B".getBytes(ZMQ.CHARSET));

                while (!Thread.currentThread().isInterrupted()) {
                    String address = subscriber.recvStr();
                    String contents = subscriber.recvStr();
                    System.out.println(address + " : " + contents);
                }
            }
        });
    }

    public static void publisher() {
        try (ZContext context = new ZContext()) {
            ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5563");

            while (!Thread.currentThread().isInterrupted()) {
                publisher.sendMore("topic.A");
                publisher.send("We don't want to see this");
                publisher.sendMore("topic.B");
                publisher.send("We would like to see this");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
