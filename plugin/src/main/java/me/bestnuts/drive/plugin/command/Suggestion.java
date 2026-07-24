package me.bestnuts.drive.plugin.command;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public interface Suggestion {

    void suggest(
            CommandContext context,
            SuggestionsBuilder builder
    );
}
