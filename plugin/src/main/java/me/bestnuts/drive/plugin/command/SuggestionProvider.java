package me.bestnuts.drive.plugin.command;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface SuggestionProvider {

    CompletableFuture<Suggestions> suggest(
            CommandContext context,
            SuggestionsBuilder builder
    );
}
