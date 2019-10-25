package br.com.poc.kafkasparkcassandra;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import scala.Tuple2;

public class App {

    public static void main(String[] args) throws InterruptedException {
        Logger.getLogger("org")
                .setLevel(Level.OFF);
        Logger.getLogger("akka")
                .setLevel(Level.OFF);

        Map<String, Object> kafkaParams = new HashMap<>();
        System.out.println("here");
        kafkaParams.put("bootstrap.servers", "localhost:29092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList("messages");

        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local[2]");
        sparkConf.setAppName("WordCountingApp");
        sparkConf.set("spark.cassandra.connection.host", "localhost");
        sparkConf.set("spark.cassandra.connection.port", "9042");
        sparkConf.set("spark.cassandra.auth.username", "cassandra");
        sparkConf.set("spark.cassandra.auth.password", "cassandra");
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1
        ));

        JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(streamingContext, LocationStrategies.PreferConsistent(), ConsumerStrategies.<String, String> Subscribe(topics, kafkaParams));

        JavaPairDStream<String, Calculation> results = messages.mapToPair(record -> {
            System.out.println(record);
            return new Tuple2<>(record.key(), new ObjectMapper().readValue(record.value(), Calculation.class));});

        JavaDStream<Calculation> calculations = results.map(tuple2 -> tuple2._2());

        JavaPairDStream<Integer, Double> wordCounts = calculations.mapToPair(c -> new Tuple2<>(c.getId(), exeCalc(c)))
                .reduceByKey((i1, i2) -> i1 + i2);

        ArrayList<Map<Integer, Double>> aux = new ArrayList<>();
        wordCounts.foreachRDD(javaRdd -> {
            Map<Integer, Double> countMap = javaRdd.collectAsMap();
            if(!countMap.isEmpty()) aux.add(countMap);
            for (Integer key : countMap.keySet()) {
                System.out.println(key);
                /*List<Word> wordList = Arrays.asList(new Word(key, countMap.get(key)));
                JavaRDD<Word> rdd = streamingContext.sparkContext()
                        .parallelize(wordList);

                javaFunctions(rdd).writerBuilder("vocabulary", "words", mapToRow(Word.class))
                        .saveToCassandra();
                System.out.println("saved!!!");*/
            }
        });

        CompletableFuture.runAsync(() -> {
        while(true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("==== START ====");
            for(Map<Integer, Double> map : aux) {
                if(!map.isEmpty()) {
                    System.out.println(" ----- ");
                    for (Integer key : map.keySet()) {
                        System.out.println(key + " : " + map.get(key));
                    }
                    System.out.println(" ----- ");
                }
            }
            System.out.println("==== END ====\n");

        }
        });
        streamingContext.start();
        streamingContext.awaitTermination();

    }

    public static double exeCalc(Calculation calculation) {
        switch (calculation.getOperator()) {
            case "+":
                return calculation.getA() + calculation.getB();
            case "*":
                return calculation.getA() * calculation.getB();
            case "-":
                return calculation.getA() - calculation.getB();
            case "/":
                return calculation.getA() / calculation.getB();
            default:
                return calculation.getA() + calculation.getB();
        }
    }
}