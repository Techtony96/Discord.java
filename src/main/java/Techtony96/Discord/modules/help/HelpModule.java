package Techtony96.Discord.modules.help;

import Techtony96.Discord.api.CustomModule;
import Techtony96.Discord.api.commands.BotCommand;
import Techtony96.Discord.api.commands.CommandContext;
import Techtony96.Discord.api.commands.CommandManager;
import Techtony96.Discord.api.commands.CustomCommand;
import sx.blah.discord.modules.IModule;
import sx.blah.discord.util.EmbedBuilder;

/**
 * Created by Tony on 12/24/2016.
 */
public class HelpModule extends CustomModule implements IModule {

    public HelpModule() {
        super("Help Module", "1.1");
    }

    @BotCommand(command = "help", aliases = "h", module = "Help Module", description = "View all available commands.", usage = "!Help")
    public static void helpCommand(CommandContext cc) {
        String currentModule = "";
        boolean first = true;
        EmbedBuilder embed = new EmbedBuilder();
        StringBuilder sb = new StringBuilder();

        embed.withColor(36, 153, 153);
        embed.setLenient(true);

        for (CustomCommand command : CommandManager.getCommands()) {
            // Check if the user has permission for the command.
            if (!cc.getUser().getPermissionsForGuild(cc.getGuild()).containsAll(command.getPermissionss()))
                continue;

            if (!currentModule.equalsIgnoreCase(command.getModule())) {
                if (!currentModule.equals(""))
                    if (first) {
                        first = false;
                        embed.withTitle(currentModule);
                        embed.withDescription(sb.toString());
                    } else
                        embed.appendField(currentModule, sb.toString(), false);
                sb.setLength(0);
                currentModule = command.getModule();
            }

            if (command.isSecret())
                continue;
            if (command.getUsage().length() > 0 && command.getDescription().length() > 0) {
                sb.append(command.getUsage()).append(" | ").append(command.getDescription()).append('\n');
            } else
                sb.append(command.getUsage().length() > 0 ? command.getUsage() : "!" + command.getName() + " " + command.getDescription()).append('\n');
        }

        embed.appendField(currentModule, sb.toString(), false);

        cc.replyWith(embed.build());
    }
}