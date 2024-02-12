package com.kafka.avro.config;

import com.kafka.avro.model.Pessoa;
import com.kafka.avro.serialize.AvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfigSubscriber {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public AvroDeserializer<Pessoa> pessoaAvroDeserializer() {
        return new AvroDeserializer<>(Pessoa.class);
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, Pessoa> consumerFactory(AvroDeserializer<Pessoa> pessoaAvroDeserializer) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "kafavro_group");
        // Observe que não estamos mais definindo o deserializador de valor aqui

        return new DefaultKafkaConsumerFactory<>(
                configProps,
                new StringDeserializer(), // Usando StringDeserializer para a chave
                pessoaAvroDeserializer // Passando o AvroDeserializer para o valor
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Pessoa> kafkaListenerContainerFactory(
            DefaultKafkaConsumerFactory<String, Pessoa> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, Pessoa> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}


