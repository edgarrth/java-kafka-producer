# Java Kafka Producer PoC

## 1. Descripción

Este proyecto es una prueba de concepto (PoC) desarrollada con Spring Boot 3 y Java 21 para publicar mensajes en Apache Kafka mediante una API REST.

La aplicación expone un endpoint HTTP que recibe información de usuarios en formato JSON y la publica en un tópico Kafka llamado `user.topic`.

Actualmente el broker Kafka utilizado es un clúster administrado en Confluent Cloud utilizando autenticación SASL/SSL.

### Funcionalidades

* Exposición de API REST para recepción de usuarios.
* Publicación de eventos en Apache Kafka.
* Serialización del objeto recibido a formato JSON.
* Integración con Confluent Cloud mediante SASL_SSL.
* Construido sobre Spring Boot y Spring Kafka.

---

# 2. Arquitectura

## Diagrama de Arquitectura

```text
+------------------+
| Cliente REST     |
| Postman / App    |
+--------+---------+
         |
         | HTTP POST /users
         v
+---------------------------+
| Spring Boot Application   |
| java-kafka-producer       |
+------------+--------------+
             |
             | Kafka Producer
             |
             v
+---------------------------+
| Kafka Topic               |
| user.topic                |
+------------+--------------+
             |
             v
+---------------------------+
| Confluent Cloud Cluster   |
| Apache Kafka              |
+---------------------------+
```

## Flujo

1. Un cliente invoca el endpoint REST `/users`.
2. La aplicación recibe el payload del usuario.
3. Se genera el mensaje Kafka.
4. El mensaje es enviado al tópico `user.topic`.
5. Kafka almacena el evento para su posterior procesamiento por consumidores.

---

# 3. Estructura del Proyecto

```text
java-kafka-producer
│
├── pom.xml
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.kafkaproducerpoc
│   │   │
│   │   │   ├── KafkaProducerPoCApplication.java
│   │   │   │
│   │   │   ├── controller
│   │   │   │   └── ConsumerController.java
│   │   │   │
│   │   │   └── domain
│   │   │       └── User.java
│   │   │
│   │   └── resources
│   │       └── application.yml
│   │
│   └── test
│
└── README.md
```

---

# 4. Componentes Principales

## KafkaProducerPoCApplication

Clase principal de Spring Boot.

```java
@SpringBootApplication
public class KafkaProducerPoCApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaProducerPoCApplication.class, args);
    }
}
```

Responsabilidades:

* Inicializar Spring Boot.
* Levantar el contexto de la aplicación.
* Registrar los componentes Spring.

---

## ConsumerController

> Nota: Aunque se llama ConsumerController, realmente implementa la lógica de un Producer.

Archivo:

```text
controller/ConsumerController.java
```

Endpoint:

```http
POST /users
```

Responsabilidades:

* Recibir solicitudes HTTP.
* Construir el mensaje Kafka.
* Publicar el evento en Kafka usando KafkaTemplate.

Código principal:

```java
kafkaTemplate.send(
    TOPIC,
    message.getId(),
    message.toJson()
);
```

Tópico utilizado:

```text
user.topic
```

---

## User

Representa el modelo de negocio enviado al tópico Kafka.

Campos:

| Campo   | Tipo   |
| ------- | ------ |
| id      | String |
| nombres | String |
| edad    | int    |
| rol     | String |

Ejemplo:

```json
{
  "id": "1001",
  "nombres": "Juan Perez",
  "edad": 30,
  "rol": "ADMIN"
}
```

---

## application.yml

Contiene la configuración de conexión hacia Kafka.

Configuraciones relevantes:

```yaml
spring:
  kafka:
    producer:
      bootstrap-servers: <broker>
      key-serializer: StringSerializer
      value-serializer: JsonSerializer
```

Seguridad:

```yaml
security.protocol=SASL_SSL
sasl.mechanism=PLAIN
```

---

# 5. Infraestructura Requerida

El proyecto no utiliza base de datos.

Infraestructura necesaria:

| Componente      | Requerido |
| --------------- | --------- |
| Java 21         | Sí        |
| Maven 3.9+      | Sí        |
| Apache Kafka    | Sí        |
| Confluent Cloud | Opcional  |
| Base de Datos   | No        |

---

# 6. Levantar Infraestructura Local

Si no se utiliza Confluent Cloud, puede ejecutarse Kafka localmente mediante Docker.

## Docker Compose

```yaml
services:

  zookeeper:
    image: confluentinc/cp-zookeeper:7.6.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

  kafka:
    image: confluentinc/cp-kafka:7.6.0
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

Levantar:

```bash
docker compose up -d
```

Crear tópico:

```bash
docker exec -it kafka kafka-topics \
 --create \
 --topic user.topic \
 --bootstrap-server localhost:9092
```

---

# 7. Compilación

## Descargar dependencias

```bash
mvn clean install
```

## Ejecutar pruebas

```bash
mvn test
```

## Empaquetar

```bash
mvn clean package
```

Generará:

```text
target/java-kafka-producer-0.0.1-SNAPSHOT.jar
```

---

# 8. Ejecución

## Desde Maven

```bash
mvn spring-boot:run
```

## Desde JAR

```bash
java -jar target/java-kafka-producer-0.0.1-SNAPSHOT.jar
```

La aplicación iniciará en:

```text
http://localhost:8080
```

---

# 9. Uso de la API

## Crear Usuario

### Request

```http
POST /users
Content-Type: application/json
```

Body:

```json
{
  "id": "1001",
  "nombres": "Juan Perez",
  "edad": 30,
  "rol": "ADMIN"
}
```

