package com.kafka.avro.serialize;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroSerializer<T extends GenericRecord> implements Serializer<T> {

    @Override
    public byte[] serialize(String topic, T data) {
        byte[] result = null;

        if (data != null) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BinaryEncoder binaryEncoder =
                        EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
                SpecificDatumWriter<T> datumWriter = new SpecificDatumWriter<>(data.getSchema());
                datumWriter.write(data, binaryEncoder);
                binaryEncoder.flush();
                byteArrayOutputStream.close();

                result = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                throw new SerializationException("Can't serialize data='" + data + "' for topic='" + topic + "'", e);
            }
        }
        return result;
    }
}
