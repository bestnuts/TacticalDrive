package me.bestnuts.plugin.command;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public interface Suggestion {

    void suggest(
            CommandContext context,
            SuggestionsBuilder builder
    );
}
