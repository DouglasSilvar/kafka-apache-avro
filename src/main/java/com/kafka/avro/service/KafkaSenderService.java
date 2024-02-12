package com.kafka.avro.service;

import com.kafka.avro.model.Pessoa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class KafkaSenderService {

    private final KafkaTemplate<String, Pessoa> kafkaTemplate;
    private final String TOPIC = "kafavro"; // Nome do tópico conforme definido no Docker Compose

    @Autowired
    public KafkaSenderService(KafkaTemplate<String, Pessoa> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 2000)
    public void sendPessoaRecord() {
        log.info("Iniciando envio de mensagem");
        Pessoa pessoa = createPessoa();
        kafkaTemplate.send(TOPIC, pessoa);
        log.info("Enviou pessoa para o tópico Kafka: " + pessoa);
    }

    private Pessoa createPessoa() {
        List<Pessoa> pessoas = Arrays.asList(
                new Pessoa("1", "João Silva", 28, "1111-2222", "joao@example.com", "Rua A", "São Paulo", "SP", "12345-678", "Brasil"),
                new Pessoa("2", "Maria Souza", 32, "3333-4444", "maria@example.com", "Rua B", "Rio de Janeiro", "RJ", "98765-432", "Brasil"),
                new Pessoa("3", "Carlos Pereira", 45, "5555-6666", "carlos@example.com", "Rua C", "Belo Horizonte", "MG", "11223-445", "Brasil")
        );
        Random random = new Random();
        return pessoas.get(random.nextInt(pessoas.size()));
    }
}
