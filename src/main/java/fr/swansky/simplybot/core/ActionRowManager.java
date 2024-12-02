package fr.swansky.simplybot.core;

import fr.swansky.simplybot.core.listeners.ActionRowListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.components.ActionComponent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/***
 * Class to manage ActionRow, it's a simple way to manage button interaction
 * This action row manager is not serializable and will not save the state of the action row after a restart
 *
 */
public class ActionRowManager {
    private final ConcurrentMap<String, ActionHandler<?>> actionHandlerMap = new ConcurrentHashMap<>();

    public ActionRowManager(JDA jda) {
        jda.addEventListener(new ActionRowListener(this, jda));
    }

    public ItemComponent createButton(ButtonBuilder builder, ActionHandler<ButtonInteractionEvent> handler) {
        String id = createUUID();

        ActionComponent actionRow = builder.createIdItem(id);
        actionHandlerMap.put(actionRow.getId(), handler);
        return actionRow;
    }

    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    public <T extends GenericComponentInteractionCreateEvent> boolean handle(T event) {
        ActionHandler<T> actionHandler = (ActionHandler<T>) actionHandlerMap.get(event.getComponentId());
        if (actionHandler != null) {
            actionHandler.handle(event);
            return true;
        }
        return false;
    }


    public interface ButtonBuilder {
        ActionComponent createIdItem(String id);
    }

    public interface ActionHandler<T extends GenericComponentInteractionCreateEvent> {
        void handle(T event);
    }
}
