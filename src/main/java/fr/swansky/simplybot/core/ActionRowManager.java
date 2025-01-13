package fr.swansky.simplybot.core;

import fr.swansky.simplybot.core.listeners.ActionRowListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/***
 * Class to manage ActionRow, it's a simple way to manage button interaction
 * This action row manager is not serializable and will not save the state of the action row after a restart
 *
 */
public class ActionRowManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionRowManager.class);
    private final ConcurrentMap<String, ActionHandler<? extends Event>> actionHandlerMap = new ConcurrentHashMap<>();

    public ActionRowManager(JDA jda) {
        jda.addEventListener(new ActionRowListener(this, jda));
    }

    /**
     * Create a button with a given builder
     *
     * @param builder the builder interface to create your own button but with a generated id
     * @param handler the handler to handle the button interaction
     * @return the button created with the given builder
     */
    public Button createButton(ButtonBuilder builder, ButtonActionHandler handler) {
        String id = createUUID();

        Button actionRow = builder.createIdItem(id);
        actionHandlerMap.put(actionRow.getId(), handler);
        return actionRow;
    }

    /**
     * Wrapper to create modal
     *
     * @param title the title of the modal
     * @return the modal builder
     */
    public Modal.Builder createModal(String title, ModalActionHandler handler) {
        String id = createUUID();
        actionHandlerMap.put(id, handler);
        return Modal.create(id, title);
    }

    private String createUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * This method is made to handle event from the action row
     * This method support ButtonInteractionEvent and ModalInteractionEvent
     *
     * @param event the event to handle
     * @return true if the event is handled
     */
    public <T extends Event> boolean handle(T event) {
        switch (event) {
            case ButtonInteractionEvent buttonInteractionEvent -> {
                ActionHandler<? extends Event> actionHandler = actionHandlerMap.get(buttonInteractionEvent.getComponentId());
                if (actionHandler instanceof ButtonActionHandler buttonActionHandler) {
                    buttonActionHandler.handle(buttonInteractionEvent);
                    return true;
                }
                return false;
            }
            case ModalInteractionEvent modalInteractionEvent -> {
                ActionHandler<? extends Event> actionHandler = actionHandlerMap.get(modalInteractionEvent.getModalId());
                if (actionHandler instanceof ModalActionHandler modalActionHandler) {
                    modalActionHandler.handle(modalInteractionEvent);
                    return true;
                }
                return false;
            }
            default -> {
                LOGGER.warn("Event {} is not supported", event.getClass().getName());
                return false;
            }
        }
    }

    /**
     * Interface to create a button
     * This interface is used to create the button you want to use
     * you can return the value from Button.primary(id, label) or other type of button.
     *
     * @see Button
     */
    public interface ButtonBuilder {
        /**
         * Create button with a given id
         *
         * @param id the generated id
         * @return the button
         */
        Button createIdItem(String id);
    }


    private interface ActionHandler<T extends Event> {
        void handle(T event);
    }

    /**
     * Interface of the method will be called when the button is interacted
     */
    public interface ModalActionHandler extends ActionHandler<ModalInteractionEvent> {
    }

    /**
     * Interface of the method will be called when the modal is interacted
     */
    public interface ButtonActionHandler extends ActionHandler<ButtonInteractionEvent> {
    }
}
