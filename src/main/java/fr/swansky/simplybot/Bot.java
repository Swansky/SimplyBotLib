package fr.swansky.simplybot;

import fr.swansky.core.commands.CommandManager;
import fr.swansky.simplybot.core.TokenProvider;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Bot {
    private final TokenProvider<String> tokenProvider;
    private final Logger logger = LoggerFactory.getLogger(Bot.class);
    protected JDA jda;
    protected CommandManager commandManager;

    public Bot(TokenProvider<String> tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public Bot(TokenProvider<String> tokenProvider, CommandManager commandManager) {
        this.tokenProvider = tokenProvider;
        this.commandManager = commandManager;
    }

    public void provideCommandManager(CommandManager commandManager) {
        if (jda != null) {
            throw new IllegalArgumentException("You can't configurer commandManager after a bot init()");
        }
        this.commandManager = commandManager;
    }

    public void init() {
        try {
            JDABuilder builder = JDABuilder.createDefault(tokenProvider.getToken());
            if (commandManager != null) builder = builder.addEventListeners(commandManager);
            config(builder);
            jda = builder.build().awaitReady();
            if (commandManager != null) {
                commandManager.registerCommands(jda);
            }
        } catch (Exception e) {
            logger.error("Impossible to init bot", e);
        }
    }

    protected abstract void config(JDABuilder builder);

}
