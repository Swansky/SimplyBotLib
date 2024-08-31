package fr.swansky.simplybot;

import fr.swansky.simplybot.core.JsonConfigManager;
import fr.swansky.simplybot.core.TokenProvider;

import java.io.IOException;

public class SettingsTokenProvider implements TokenProvider<String> {
    private final JsonConfigManager configManager;
    private final String configFilePath;

    public SettingsTokenProvider(String configFilePath) {
        this.configManager = new JsonConfigManager();
        this.configFilePath = configFilePath;
    }

    @Override
    public String getToken() {
        try {
            Settings config = configManager.loadConfig(configFilePath, Settings.class, new Settings("<Token>"));
            return config.getToken();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the configuration file.", e);
        }
    }
}
