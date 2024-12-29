package de.varilx.utils;

import lombok.experimental.UtilityClass;

import java.io.*;

@UtilityClass
public class ResourceUtils {

    public void copyFileFromResources(Class<?> applicationClass, String destinationPath, String fileName) {
        try (InputStream inputStream = applicationClass.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                File destinationDir = new File(destinationPath);
                if (!destinationDir.exists()) {
                    boolean created = destinationDir.mkdirs();
                    if (!created) {
                        throw new IOException("Failed to create directory: " + destinationPath);
                    }
                }
                File destinationFile = new File(destinationPath, fileName);
                try (OutputStream outputStream = new FileOutputStream(destinationFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                }
            } else {
                System.out.println("File is null!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error copying file from resources: " + e.getMessage(), e);
        }
    }

}
