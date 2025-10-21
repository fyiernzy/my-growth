package com.ifast.ipaymy.oauth2.utils.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class FileUtils {

    public static boolean isParentPathExist(Path path) {
        return Files.exists(path.getParent());
    }

    public static void writeAtomically(Path path, String content) throws IOException {
        Path tempPath = path.resolveSibling(path.getFileName().toString() + ".tmp");
        try {
            Files.writeString(
                tempPath,
                content,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW,
                StandardOpenOption.WRITE
            );
        } catch (FileAlreadyExistsException ignored) {
        }
        Files.move(
            tempPath,
            path,
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.ATOMIC_MOVE
        );
    }

    public static void createParentPathIfNotExist(Path path) {
        try {
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path getSubprojectRoot(Class<?> clazz) {
        try {
            String location = clazz.getProtectionDomain().getCodeSource().getLocation().toURI()
                .getPath();
            Path classPath = Paths.get(location);
            while (classPath != null && !Files.exists(classPath.resolve("build.gradle"))
                   && !Files.exists(classPath.resolve("pom.xml"))) {
                classPath = classPath.getParent();
            }
            return classPath != null ? classPath : Paths.get(System.getProperty("user.dir"));
        } catch (Exception e) {
            return Paths.get(System.getProperty("user.dir"));
        }
    }
}
