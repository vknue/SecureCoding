package hr.algebra.glowlog.service;

import hr.algebra.glowlog.entity.User;
import hr.algebra.glowlog.enums.Role;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@Service
public class SerializationService {

    private static final List<String> WHITELIST = Arrays.asList(
            User.class.getName(),
            Role.class.getName(),
            "java.lang.Enum"
    );

    public void serialize(User user, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(user);

        }
    }

    public Object deserialize(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[4];
            if (fis.read(header) != 4 ||
                    header[0] != (byte) 0xAC || header[1] != (byte) 0xED ||
                    header[2] != 0x00 || header[3] != 0x05) {
                throw new SecurityException("Not correct binary validation code");
            }
        }
        try (WhitelistObjectInputStream wois = new WhitelistObjectInputStream(new FileInputStream(file))) {
            return wois.readObject();
        }
    }

    private static class WhitelistObjectInputStream extends ObjectInputStream {
        public WhitelistObjectInputStream(InputStream in) throws IOException {
            super(in);

        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            if (!WHITELIST.contains(desc.getName())) {
                throw new InvalidClassException("Unauthorized deserialization attempt", desc.getName());
            }
            return super.resolveClass(desc);
        }
    }
}