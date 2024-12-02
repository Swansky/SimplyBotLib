package fr.swansky.simplybot.core.listeners;

import fr.swansky.simplybot.core.ActionRowManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionRowListener extends ListenerAdapter {

    private final ActionRowManager actionRowManager;
    private final Logger logger = LoggerFactory.getLogger(ActionRowListener.class);
    private final JDA jda;


    public ActionRowListener(ActionRowManager actionRowManager, JDA jda) {
        this.actionRowManager = actionRowManager;
        this.jda = jda;
    }

    @Override
    public void onGenericComponentInteractionCreate(GenericComponentInteractionCreateEvent event) {

        Member member = event.getMessage().getMember();
        if (member == null) return;
        User user = member.getUser();

        if (isNotValidUser(user)) {
            return;
        }
        event.deferReply().queue();
        if (!actionRowManager.handle(event)) {
            event.getMessage().reply("No action found for this button").queue();
            logger.warn("No action row found for {}", event.getComponentId());
        }
    }

    private boolean isNotValidUser(User user) {
        SelfUser selfUser = jda.getSelfUser();
        return !user.isBot() || selfUser.getIdLong() != user.getIdLong();
    }
}
