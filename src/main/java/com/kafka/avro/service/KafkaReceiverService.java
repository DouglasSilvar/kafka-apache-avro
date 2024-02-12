package com.kafka.avro.service;

import com.kafka.avro.model.Pessoa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaReceiverService {

    @KafkaListener(topics = "kafavro", groupId = "kafavro_group")
    public void listen(Pessoa pessoa, Acknowledgment ack) {
        log.info("----------------------------- Recebida mensagem no tópico kafavro: {}", pessoa);
        ack.acknowledge();
    }
}
