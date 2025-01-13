package fr.swansky;

import fr.swansky.simplybot.core.ActionRowManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ActionRowManagerTest {
    @Mock
    private JDA jdaMock;
    @Mock
    private ButtonInteractionEvent buttonInteractionEvent;
    @Mock
    private ModalInteractionEvent modalInteractionEvent;
    @Mock
    private MessageReceivedEvent messageReceivedEvent;

    private ActionRowManager actionRowManager;

    private boolean call = false;


    @BeforeEach
    public void beforeEach() {
        // Given
        MockitoAnnotations.openMocks(this);
        actionRowManager = new ActionRowManager(jdaMock);
        call = false;
    }


    @Test
    void buttonTest() {
        // Given
        final String title = "Test";
        // When
        Button buttonComponent = actionRowManager.createButton((id -> Button.primary(id, title)), this::handler);
        when(buttonInteractionEvent.getComponentId()).thenReturn(buttonComponent.getId());

        // Then
        assertNotNull(buttonComponent);
        call = false;
        assertTrue(actionRowManager.handle(buttonInteractionEvent));
        assertTrue(call);
        assertEquals(title, buttonComponent.getLabel());

        when(buttonInteractionEvent.getComponentId()).thenReturn("wrongId");
        call = false;
        assertFalse(actionRowManager.handle(buttonInteractionEvent));
        assertFalse(call);
    }

    public void handler(ButtonInteractionEvent buttonInteractionEvent) {
        call = true;
    }

    @Test
    void modalTest() {
        // Given
        final String title = "Test";
        // When
        Modal.Builder modal = actionRowManager.createModal(title, this::handlerModal);
        when(modalInteractionEvent.getModalId()).thenReturn(modal.getId());
        // Then
        assertNotNull(modal);
        call = false;
        assertTrue(actionRowManager.handle(modalInteractionEvent));
        assertTrue(call);
        assertEquals(title, modal.getTitle());

        when(modalInteractionEvent.getModalId()).thenReturn("wrongId");
        call = false;
        assertFalse(actionRowManager.handle(modalInteractionEvent));
        assertFalse(call);
    }

    private void handlerModal(ModalInteractionEvent modalInteractionEvent) {
        call = true;
    }

    @Test
    void wrongEventTest() {
        // Given
        // When
        boolean handled = actionRowManager.handle(messageReceivedEvent);
        // Then
        assertFalse(handled);
    }

}
