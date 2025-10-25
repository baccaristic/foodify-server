package com.foodify.imageservice;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {
    private final StorageProperties properties;
    private final Path rootLocation;

    public ImageStorageService(StorageProperties properties) throws IOException {
        this.properties = Objects.requireNonNull(properties, "properties");
        this.rootLocation = Path.of(properties.getLocation()).toAbsolutePath().normalize();
        Files.createDirectories(rootLocation);
    }

    public StoredImage store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String identifier = UUID.randomUUID().toString().replace("-", "");
        if (StringUtils.hasText(extension)) {
            identifier = identifier + "." + extension.toLowerCase(Locale.ROOT);
        }

        Path destination = rootLocation.resolve(identifier).normalize();
        if (!destination.startsWith(rootLocation)) {
            throw new IOException("Cannot store file outside of the configured directory");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        }

        return new StoredImage(identifier, destination);
    }

    public Path resolve(String identifier) {
        return rootLocation.resolve(identifier).normalize();
    }

    public boolean delete(String identifier) throws IOException {
        Path path = resolve(identifier);
        if (!Files.exists(path)) {
            return false;
        }
        return FileSystemUtils.deleteRecursively(path);
    }

    public record StoredImage(String id, Path path) {}
}
