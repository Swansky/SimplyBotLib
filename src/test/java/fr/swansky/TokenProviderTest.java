package fr.swansky;

import fr.swansky.simplybot.SettingsTokenProvider;
import fr.swansky.simplybot.core.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class TokenProviderTest {

    private static final String CONFIG_FILE_PATH = "test-settings.json";

    @BeforeEach
    void setUp() {
        File configFile = new File(CONFIG_FILE_PATH);
        if (configFile.exists()) {
            assertTrue(configFile.delete(), "Failed to delete test settings file");
        }
    }

    @Test
    void testDirectProvider() {
        String expectedToken = "direct-token";
        TokenProvider<String> tokenProvider = TokenProvider.directProvider(expectedToken);

        assertEquals(expectedToken, tokenProvider.getToken(), "DirectProvider did not return the expected token");
    }

    @Test
    void testSettingsTokenProviderWithExistingFile() throws IOException {
        String jsonContent = "{\"token\":\"file-token\"}";
        writeFile(jsonContent);

        SettingsTokenProvider tokenProvider = new SettingsTokenProvider(CONFIG_FILE_PATH);

        assertEquals("file-token", tokenProvider.getToken(), "SettingsTokenProvider did not return the expected token from file");
    }

    @Test
    void testSettingsTokenProviderWithMissingFile() {
        SettingsTokenProvider tokenProvider = new SettingsTokenProvider(CONFIG_FILE_PATH);

        String token = tokenProvider.getToken();
        assertEquals("<Token>", token, "SettingsTokenProvider did not return the expected default token");

        try {
            String content = new String(Files.readAllBytes(Paths.get(CONFIG_FILE_PATH)));
            assertEquals("{\"token\":\"<Token>\"}", content, "SettingsTokenProvider did not correctly save the default token to file");
        } catch (IOException e) {
            fail("Failed to read settings file: " + e.getMessage());
        }
    }

    @Test
    void testSettingsTokenProviderWithInvalidFile() {
        File invalidFile = new File(CONFIG_FILE_PATH);
        try {
            assertTrue(invalidFile.createNewFile(), "Failed to create invalid test settings file");
        } catch (IOException e) {
            fail("Failed to create invalid test settings file: " + e.getMessage());
        }

        try (FileWriter writer = new FileWriter(invalidFile)) {
            writer.write("Invalid JSON Content");
        } catch (IOException e) {
            fail("Failed to write invalid content to test settings file: " + e.getMessage());
        }

        SettingsTokenProvider tokenProvider = new SettingsTokenProvider(CONFIG_FILE_PATH);

        assertThrows(RuntimeException.class, tokenProvider::getToken, "Expected a RuntimeException due to invalid configuration file");
    }

    private void writeFile(String content) throws IOException {
        try (FileWriter writer = new FileWriter(TokenProviderTest.CONFIG_FILE_PATH)) {
            writer.write(content);
        }
    }
}

