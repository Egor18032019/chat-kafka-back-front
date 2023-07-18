# chat-kafka-back-front
---
Приложение для чата использующее: Kafka, Spring Boot, ReactJS и WebSockets
---
***
# Сделать
  * TODO -> Добавить в Докер фронт
  * TODO -> Изменить чат
  * TODO -> Сделать такой же для RabbitMq

# Запуск Докера в ручную

docker network create kafkanet
---

* Убеждаемся что создалась
  docker network list //TODO написать для чего
*
    * Запускаем зукипер

docker run -d --network=kafkanet --name=zookeeper -e ZOOKEEPER_CLIENT_PORT=2181 -e ZOOKEEPER_TICK_TIME=2000 -p 2181:2181 confluentinc/cp-zookeeper
---

*
    * Запуска контенера с кафкой
---

docker run -d --network=kafkanet --name=kafka -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 -p 9092:9092 confluentinc/cp-kafka
---

*
    * Чтобы убедиться в отсутствии ошибок, можно вывести лог docker logs kafka
    * Подключаемся к контейнеру kafka
    * docker exec -it kafka bash
      Создам топик  (kafka-chat)

---
/bin/kafka-topics --create --topic kafka-chat --bootstrap-server kafka:9092
---
Что бы убедиться что создался выводим весь список топиков
---
/bin/kafka-topics --bootstrap-server=localhost:9092 --list
---

* Состояние топика
  /bin/kafka-topics --describe --topic kafka-chat --bootstrap-server localhost:9092
  или
  /bin/kafka-topics --describe --topic kafka-chat --bootstrap-server kafka:9092

* Генерация сообщений через консоль (timestamp указывать не обязательно)

---
/bin/kafka-console-producer --topic demo-topic --bootstrap-server kafka:9092
{"sender": "Hello, server!","content": "content","timestamp":"2023-07-10T15:03:52.082974700"}
---

* Чтение сообщений из топика

---
/bin/kafka-console-consumer --topic demo-topic --from-beginning --bootstrap-server kafka:9092
---
