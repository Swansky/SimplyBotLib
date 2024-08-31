package fr.swansky.simplybot.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;

public class JsonConfigManager {

    private final ObjectMapper objectMapper;

    public JsonConfigManager() {
        this.objectMapper = new ObjectMapper();
    }

    public JsonConfigManager(SimpleModule customModule) {
        this();
        this.objectMapper.registerModule(customModule);
    }

    public JsonConfigManager(SimpleModule... customModules) {
        this();
        this.objectMapper.registerModules(customModules);
    }

    public <T> void saveConfig(T configObject, String filePath) throws IOException {
        File file = new File(filePath);
        createParentDirectoriesIfNotExist(file);
        objectMapper.writeValue(file, configObject);
    }

    public <T> T loadConfig(String filePath, Class<T> configClass) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("Configuration file not found: " + filePath);
        }

        return objectMapper.readValue(file, configClass);
    }

    public <T> T loadConfig(String filePath, Class<T> configClass, T defaultObject) throws IOException {
        File file = new File(filePath);

        if (!file.exists()) {
            return handleMissingFile(file, configClass, defaultObject);
        }

        return objectMapper.readValue(file, configClass);
    }


    private <T> T handleMissingFile(File file, Class<T> configClass, T defaultObject) throws IOException {
        if (defaultObject != null) {
            saveConfig(defaultObject, file.getPath());
            return defaultObject;
        } else {
            throw new IOException("Configuration file not found and no default object provided: " + file.getPath());
        }
    }


    public void addCustomSerializerDeserializer(SimpleModule customModule) {
        objectMapper.registerModule(customModule);
    }


    public <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }


    public <T> T fromJson(String jsonString, Class<T> configClass) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, configClass);
    }


    private void createParentDirectoriesIfNotExist(File file) throws IOException {
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create directories: " + parentDir.getAbsolutePath());
            }
        }

        // Create the file if it does not exist
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Failed to create file: " + file.getAbsolutePath());
        }
    }
}
