package me.bestnuts.drive.plugin.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class StringArgument extends ArgumentNode<String> {

    public StringArgument(String name) {
        super(name);
    }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, ?> build() {

        RequiredArgumentBuilder<CommandSourceStack, String> builder =
                RequiredArgumentBuilder.argument(
                        name,
                        StringArgumentType.string()
                );

        if (suggestionProvider != null) {

            builder.suggests((ctx, suggestions) ->
                    suggestionProvider.suggest(
                            new CommandContext(ctx),
                            suggestions
                    )
            );

        }

        return builder;
    }
}
