package me.bestnuts.plugin.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class CommandNode {

    private final String name;

    private final List<CommandNode> children = new ArrayList<>();
    private final List<ArgumentNode<?>> arguments = new ArrayList<>();

    private Consumer<CommandContext> executor;

    public CommandNode(String name) {
        this.name = name;
    }

    public CommandNode child(CommandNode node) {
        children.add(node);
        return this;
    }

    public CommandNode argument(ArgumentNode<?> argument) {
        arguments.add(argument);
        return this;
    }

    public CommandNode playerExecute(Consumer<CommandContext> executor) {
        this.executor = executor;
        return this;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {

        LiteralArgumentBuilder<CommandSourceStack> literal =
                LiteralArgumentBuilder.literal(name);

        for (CommandNode child : children) {
            literal.then(child.build());
        }

        if (!arguments.isEmpty()) {
            literal.then(buildArguments(0));
        }

        return literal;
    }

    private ArgumentBuilder<CommandSourceStack, ?> buildArguments(int index) {

        ArgumentNode<?> node = arguments.get(index);

        ArgumentBuilder<CommandSourceStack, ?> builder = node.build();

        if (index + 1 < arguments.size()) {

            builder.then(buildArguments(index + 1));

        } else if (executor != null) {

            builder.executes(ctx -> {

                executor.accept(new CommandContext(ctx));

                return 1;

            });

        }

        return builder;
    }
}
