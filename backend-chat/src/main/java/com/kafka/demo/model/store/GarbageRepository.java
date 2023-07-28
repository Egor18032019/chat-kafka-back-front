package com.kafka.demo.model.store;

import com.kafka.demo.model.Message;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
/**
 * Как бы база данных
 */
public class GarbageRepository {

    private static final List<byte[]> GARBAGE_ENTITIES = new ArrayList<>();

    public Long countAll() {
        return (long) GARBAGE_ENTITIES.size();
    }

    public List<byte[]> findAll() {
        return GARBAGE_ENTITIES;
    }


    public void add(byte[] data) {
        GARBAGE_ENTITIES.add(data);
    }

    public void add(Message entity) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ObjectOutputStream out = null;
            out = new ObjectOutputStream(bos);
            out.writeObject(entity);
            out.flush();
            byte[] arrBytes = bos.toByteArray();
            GARBAGE_ENTITIES.add(arrBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // ignore close exception
    }
}
