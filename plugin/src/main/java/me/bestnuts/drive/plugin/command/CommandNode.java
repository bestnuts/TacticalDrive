package me.bestnuts.drive.plugin.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class CommandNode {

    private final String name;

    private final List<CommandNode> children = new ArrayList<>();
    private final List<ArgumentNode<?>> arguments = new ArrayList<>();

    private Consumer<CommandContext> executor;
    private boolean playerOnly;

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

    public CommandNode execute(Consumer<CommandContext> executor) {
        this.executor = executor;
        this.playerOnly = false;
        return this;
    }

    public CommandNode playerExecute(Consumer<CommandContext> executor) {
        this.executor = executor;
        this.playerOnly = true;
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
        } else if (executor != null) {
            literal.executes(this::run);
        }

        return literal;
    }

    private int run(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        CommandContext context = new CommandContext(ctx);

        if (playerOnly && !(context.sender() instanceof Player)) {
            context.reply("플레이어만 사용할 수 있는 명령어입니다.");
            return 0;
        }

        executor.accept(context);
        return 1;
    }

    private ArgumentBuilder<CommandSourceStack, ?> buildArguments(int index) {

        ArgumentNode<?> node = arguments.get(index);

        ArgumentBuilder<CommandSourceStack, ?> builder = node.build();

        if (index + 1 < arguments.size()) {

            builder.then(buildArguments(index + 1));

        } else if (executor != null) {

            builder.executes(this::run);

        }

        return builder;
    }
}
