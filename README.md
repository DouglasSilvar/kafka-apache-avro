# Kafka Apache Avro Project

## Descrição

Este projeto é uma aplicação Spring Boot que demonstra o uso do Apache Kafka para publicar e consumir mensagens serializadas com Avro.

## Pré-requisitos

- Java 11 ou superior
- Docker e Docker Compose
- Maven (opcional, para construção do projeto)

## Configuração e Execução

### Passo a Passo com Docker Compose

1. **Clone o projeto** e navegue até o diretório do projeto.

2. **Inicie os serviços Kafka, Zookeeper e Kafdrop** usando Docker Compose:

```bash
docker-compose up -d
```

 A aplicação automaticamente publicará mensagens no tópico Kafka kafavro e consumirá essas mensagens. Verifique os logs da aplicação e do Kafdrop para ver as mensagens sendo processadas.