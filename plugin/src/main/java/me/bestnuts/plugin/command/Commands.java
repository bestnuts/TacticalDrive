package me.bestnuts.plugin.command;

public final class Commands {

    private Commands() {
    }

    public static CommandNode command(String name) {
        return new CommandNode(name);
    }
}
