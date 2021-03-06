package io.mywish.bot.commands;

import io.mywish.bot.service.BotCommand;
import io.mywish.bot.service.ChatContext;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BotCommandHelp implements BotCommand {
    @Getter
    private final String name = "/help";
    @Getter
    private final String usage = "";
    @Getter
    private final String description = "Command list with their arguments and descriptions";

    private final List<BotCommand> commands;

    public BotCommandHelp(List<BotCommand> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(ChatContext context, List<String> args) {
        List<String> stringCommands = commands
                .stream()
                .map(cmd ->
                        cmd.getName() + " " + cmd.getUsage() + "\n\t" + cmd.getDescription()
                )
                .collect(Collectors.toList());
        context.sendMessage(
                String.join(
                        "\n\n",
                        stringCommands
                )
        );
    }
}