package me.bestnuts.plugin.command;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public abstract class ArgumentNode<T> {

    protected final String name;
    protected SuggestionProvider suggestionProvider;

    protected ArgumentNode(String name) {
        this.name = name;
    }

    public ArgumentNode<T> suggests(SuggestionProvider provider) {
        this.suggestionProvider = provider;
        return this;
    }

    public ArgumentNode<T> suggests(String... values) {

        return suggests((ctx, builder) -> {

            for (String value : values) {
                builder.suggest(value);
            }

            return builder.buildFuture();

        });

    }

    public abstract RequiredArgumentBuilder<CommandSourceStack, ?> build();

}
