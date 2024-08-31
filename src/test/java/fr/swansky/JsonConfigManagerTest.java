package fr.swansky;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.swansky.simplybot.core.JsonConfigManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonConfigManagerTest {

    private JsonConfigManager jsonConfigManager;
    private final String testFilePath = "test-config.json";

    @BeforeEach
    void setUp() {
        jsonConfigManager = new JsonConfigManager();
    }

    @AfterEach
    void tearDown() {
        File file = new File(testFilePath);
        if (file.exists()) {
            assertTrue(file.delete(), "Failed to delete test config file");
        }
    }

    @Test
    void testSaveAndLoadConfig() throws IOException {
        TestConfig config = new TestConfig("test", 123);

        jsonConfigManager.saveConfig(config, testFilePath);

        TestConfig loadedConfig = jsonConfigManager.loadConfig(testFilePath, TestConfig.class);

        assertEquals(config, loadedConfig, "Loaded config does not match saved config");
    }

    @Test
    void testLoadConfigWithDefault() throws IOException {
        TestConfig defaultConfig = new TestConfig("default", 456);

        TestConfig loadedConfig = jsonConfigManager.loadConfig(testFilePath, TestConfig.class, defaultConfig);

        assertEquals(defaultConfig, loadedConfig, "Loaded config does not match default config");

        TestConfig configFromFile = jsonConfigManager.loadConfig(testFilePath, TestConfig.class);
        assertEquals(defaultConfig, configFromFile, "Config in file does not match default config");
    }

    @Test
    void testToJsonAndFromJson() throws JsonProcessingException {
        TestConfig config = new TestConfig("jsonTest", 789);

        String json = jsonConfigManager.toJson(config);

        TestConfig fromJsonConfig = jsonConfigManager.fromJson(json, TestConfig.class);

        assertEquals(config, fromJsonConfig, "Config from JSON does not match original config");
    }

    @Test
    void testCustomModule() throws JsonProcessingException {
        SimpleModule customModule = new SimpleModule();
        // For this test, we won't add any, but we ensure the module can be added

        jsonConfigManager.addCustomSerializerDeserializer(customModule);

        TestConfig config = new TestConfig("customModuleTest", 999);
        String json = jsonConfigManager.toJson(config);
        TestConfig fromJsonConfig = jsonConfigManager.fromJson(json, TestConfig.class);

        assertEquals(config, fromJsonConfig, "Config with custom module does not match original config");
    }

    @Test
    void testFileNotFound() {
        assertThrows(IOException.class, () -> {
            jsonConfigManager.loadConfig("non-existent-file.json", TestConfig.class);
        }, "Expected an IOException for a non-existent file");
    }
}

