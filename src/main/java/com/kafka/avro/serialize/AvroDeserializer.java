package com.kafka.avro.serialize;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;

public class AvroDeserializer<T extends GenericRecord> implements Deserializer<T> {
    private final Class<T> targetType;

    public AvroDeserializer(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        T result = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            BinaryDecoder binaryDecoder =
                    DecoderFactory.get().binaryDecoder(byteArrayInputStream, null);
            SpecificDatumReader<T> datumReader = new SpecificDatumReader<>(targetType.newInstance().getSchema());
            result = datumReader.read(null, binaryDecoder);
        } catch (Exception e) {
            throw new SerializationException("Can't deserialize data for topic='" + topic + "'", e);
        }
        return result;
    }
}
