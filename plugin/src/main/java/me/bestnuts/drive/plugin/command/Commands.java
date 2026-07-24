package me.bestnuts.drive.plugin.command;

public final class Commands {

    private Commands() {
    }

    public static CommandNode command(String name) {
        return new CommandNode(name);
    }
}
