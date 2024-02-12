package com.kafka.avro.serialize;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;


public class AvroDeserializer<T extends GenericRecord> implements Deserializer<T> {
    private Class<T> targetType;

    public AvroDeserializer() {
    }

    public AvroDeserializer(Class<T> targetType) {
        this.targetType = targetType;
    }

    public void setTargetType(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        if (targetType == null) {
            throw new IllegalStateException("Target type não foi definido para desserialização Avro.");
        }

        T result = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data)) {
            BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(byteArrayInputStream, null);
            SpecificDatumReader<T> datumReader = new SpecificDatumReader<>(targetType.newInstance().getSchema());
            result = datumReader.read(null, binaryDecoder);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SerializationException("Erro ao instanciar o tipo alvo para desserialização Avro", e);
        } catch (IOException e) {
            throw new SerializationException("Não foi possível desserializar os dados para o tópico '" + topic + "'", e);
        }
        return result;
    }
}
