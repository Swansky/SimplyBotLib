# SimpleBotLib

**SimpleBotLib** is a library designed to simplify the development of Discord bots. It offers several key features to
make bot creation and management easier, focusing on simplicity and efficiency.

## Features

- 🟢 **Bot Initialization and Management**: SimpleBotLib enables quick and easy setup of your Discord bot, including
  token management and initial command handling.
- 🟢 **JSON File Management**: Handles the configuration and persistence of your bot's settings using JSON files.
- 🟢 **Intelligent Wrapper for Modals and Buttons** *(Coming Soon)*: A simplified way for managing Discord modals and
  buttons.
    - 🟢 **Modals**: Create and manage modals for your bot.
    - 🟢 **Buttons**: Add buttons to your bot's messages.
- 🔴 **Default Commands** *(Coming Soon)*: A set of predefined commands to accelerate bot development.

SimpleBotLib uses the [SSCS (Simple Slash Command System)](https://github.com/swansky/SSCS) library by default, which
provides simplified slash command management.

## Installation

### Adding SimpleBotLib to Your Project

You can add SimpleBotLib to your Gradle project using the following dependency:

```gradle
implementation 'fr.swansky:SimplyBotLib:1.0.3'
```

### Using a Specific Version of SSCS

If you want to use a different version of the SSCS library, you can exclude the default version provided by SimpleBotLib
and specify your own:

```gradle
implementation ('fr.swansky:SimplyBotLib:1.0.3') {
    exclude group: 'fr.swansky', module: 'SSCS'
}
implementation 'fr.swansky:SSCS:1.0.2'
```

## Example Usage

Here is a simple example of setting up your bot with SimpleBotLib:

```java
import fr.swansky.botlib.Bot;
import fr.swansky.botlib.config.CommandManager;
import fr.swansky.botlib.config.SettingsTokenProvider;
import net.dv8tion.jda.api.JDABuilder;

public class SwansBot extends Bot {

    public SwansBot(CommandManager commandManager) {
        super(new SettingsTokenProvider("settings.json"), commandManager);
    }

    @Override
    protected void config(JDABuilder jdaBuilder) {
        // Custom bot configuration
    }
}
```

### Example Explanation

- **SettingsTokenProvider**: This token provider uses a `settings.json` file to configure your bot.
- **CommandManager**: SSCS command manager, this parameter is not mandatory.

## Example ActionRow

```java
public class Example {
    private final JDA jda;
    private final ActionRowManager actionRowManager;


    public void init() {
        actionRowManager = new ActionRowManager(jda);
        Button buttonComponent = actionRowManager.createButton((id -> Button.primary(id, title)), this::handlerButton);

        Modal.Builder modal = actionRowManager.createModal(title, this::handlerModal);
    }

    public void handlerButton(ButtonInteractionEvent event) {
        event.reply("Button clicked").queue();
    }

    public void handlerModal(ModalInteractionEvent event) {
        event.reply("Modal clicked").queue();
    }
}


```

## Roadmap

- ⚙️ Integrate default commands to further simplify development.

