
from kafka import KafkaProducer
import json
import random
import os
from time import sleep
from datetime import datetime

# Create an instance of the Kafka producer
producer = KafkaProducer(bootstrap_servers='localhost:29092',
                            value_serializer=lambda v: str(v).encode('utf-8'))

# Call the producer.send method with a producer-record


print("Ctrl+c to Stop")
i = 0
jsons = []
for file_name in os.listdir("data"):
    f = open("data/" + file_name,"r")
    print(f.read())
    jsons.append(f.read())
    f.close()

while i != 1000:
    for json in jsons:
        producer.send('messages', json)
    i+=1
    print('sent')

