package me.bestnuts.plugin.command;

public final class Arguments {

    private Arguments() {
    }

    public static StringArgument string(String name) {
        return new StringArgument(name);
    }
}
