package hr.algebra.glowlog.service;

import static org.junit.jupiter.api.Assertions.*;

import hr.algebra.glowlog.entity.Product;
import hr.algebra.glowlog.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;

class SerializationServiceTest {

    private final SerializationService serializationService = new SerializationService();

    @TempDir
    Path tempDir;

    @Test
    void testSerializeAndDeserializeSuccess() throws Exception {
        File file = tempDir.resolve("user.ser").toFile();
        User user = new User();
        user.setUsername("testuser");

        serializationService.serialize(user, file.getAbsolutePath());

        Object deserialized = serializationService.deserialize(file);

        assertTrue(deserialized instanceof User);
        assertEquals("testuser", ((User) deserialized).getUsername());
    }

    @Test
    void testDeserializeInvalidHeader() throws IOException {
        File file = tempDir.resolve("bad_header.ser").toFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(new byte[]{0x00, 0x00, 0x00, 0x00}); // Incorrect header
        }

        assertThrows(SecurityException.class, () -> serializationService.deserialize(file));
    }

    static class NotWhitelistedTempClass implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    @Test
    void testDeserializeUnauthorizedSerializableClass() throws Exception {
        File file = tempDir.resolve("unauthorized_serializable.ser").toFile();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new NotWhitelistedTempClass());
        }

        assertThrows(InvalidClassException.class, () -> serializationService.deserialize(file));
    }
}