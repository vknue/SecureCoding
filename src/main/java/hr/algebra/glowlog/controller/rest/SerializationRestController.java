package hr.algebra.glowlog.controller.rest;

import hr.algebra.glowlog.entity.Product;
import hr.algebra.glowlog.entity.User;
import hr.algebra.glowlog.service.SerializationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/serialization")
@Tag(name = "Serialization", description = "Endpoints for serializing and deserializing")
public class SerializationRestController {

    private final SerializationService serializationService;

    private final String DIRECTORY = "/tmp/serialized/";

    public SerializationRestController(SerializationService serializationService) {
        this.serializationService = serializationService;
    }

    @PostMapping("/save-user")
    @Operation(summary = "Serializes user to file")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        try {
            serializationService.serialize(user, "user_data.ser");
            return ResponseEntity.ok("SUCCESS");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("ERROR: " + e.getMessage());
        }
    }

    @GetMapping("/create-bad-file")
    public ResponseEntity<String> createBadFile() {
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream("user_data.ser"))) {
            oos.writeObject(new UnauthorizedData());
            return ResponseEntity.ok("Bad file created");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    static class UnauthorizedData implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
    }

    @GetMapping("/load-user")
    @Operation(summary = "Deserializes user from file")
    public ResponseEntity<String> loadUser() {
        try {
            File file = new File("user_data.ser");
            User user = (User) serializationService.deserialize(file);
            return ResponseEntity.ok("SUCCESS: User is " + user.getUsername());
        } catch (Exception e) {
            return ResponseEntity.status(403).body("FAILURE: " + e.getMessage());
        }
    }

    @GetMapping("/test-invalid-binary")
    @Operation(summary = "Normal files should not be allowed")
    public ResponseEntity<String> testInvalidBinary() {
        try {
            File textFile = new File("invalid.ser");
            try (java.io.FileWriter writer = new java.io.FileWriter(textFile)) {
                writer.write("Regular file made");
            }

            serializationService.deserialize(textFile);
            return ResponseEntity.ok("Test failed");
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Request blocked" + e.getMessage());
        }
    }

    @PostMapping("/test-ssrf")
    public ResponseEntity<String> testSsrf(@RequestParam String url) {
        try {
            new hr.algebra.glowlog.security.UrlValidator().validate(url);
            return ResponseEntity.ok("Request allowed to: " + url);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}
