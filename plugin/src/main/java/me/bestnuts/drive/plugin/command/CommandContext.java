package me.bestnuts.drive.plugin.command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandContext {

    private final com.mojang.brigadier.context.CommandContext<CommandSourceStack> context;

    public CommandContext(
            com.mojang.brigadier.context.CommandContext<CommandSourceStack> context
    ) {
        this.context = context;
    }

    public Player player() {
        return (Player) context.getSource().getSender();
    }

    public CommandSender sender() {
        return context.getSource().getSender();
    }

    public void reply(String message) {
        sender().sendMessage(message);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) context.getArgument(name, Object.class);
    }
}
