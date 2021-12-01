package dev.tomr.parkutil.command;

import dev.tomr.parkutil.Parkutil;
import dev.tomr.parkutil.exceptions.CommandException;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Command implements CommandExecutor {
    private final Map<String, Command> subCommands = new HashMap<>();

    @Getter
    private final String name;
    @Getter
    @Setter
    private Command superCommand = null;
    @Getter
    @Setter
    private String permission;
    @Getter
    @Setter
    private String description;

    protected Command(String name) {
        this.name = name;
    }

    protected Command(String name, Command... subCommands) {
        this.name = name;
    }

    public void registerSubCommands(Command... commands) {
        for (Command command : commands) {
            if (command.getSuperCommand() != null) {
                throw new IllegalArgumentException("The Command (" + command.getName() + ") already has a superCommand");
            }
            CommandMeta commandMeta = command.getClass().getAnnotation(CommandMeta.class);
            if (commandMeta != null) {
                command.setDescription(commandMeta.description());
                command.setPermission(commandMeta.permission());
            }
            command.setSuperCommand(this);
            this.subCommands.put(command.getName(), command);
        }
    }

    public void unregisterSubCommands(Command... commands) {
        for (Command subCommand : commands) {
            //if (!subCommand.getSuperCommand().equals(this)) continue;
            this.subCommands.remove(subCommand.getName());
            subCommand.setSuperCommand(null);
        }
    }

    public void regenerateHelpCommand() {
        if (subCommands.containsKey("help")) return;
        final Command superHelpCommand = this;
        Command help = new Command("help") {
            @Override
            public void handleCommandUnspecific(CommandSender sender, String[] args) {
                String name = Command.this.getFormattedName();
                StringBuilder msg = new StringBuilder(ChatColor.GREEN + name + " Commands:");
                if (!isSubCommandsOnly()) {
                    msg.append("\n").append(ChatColor.GREEN).append("/").append(name.toLowerCase()).append(" ").append(ChatColor.AQUA).append("- ").append(Command.this.getDescription());
                }
                for (Map.Entry<String, Command> entry : subCommands.entrySet()) {
                    msg.append("\n").append(ChatColor.GREEN).append("/").append(entry.getValue().getFormattedName()).append(" ").append(ChatColor.AQUA).append("- ").append(entry.getValue().getDescription());
                }
                sender.sendMessage(msg.toString());
            }
        };
        help.setSuperCommand(this);
        help.setDescription("Open the help menu");
        this.subCommands.put("help", help);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {
        try {
            Command subCommand = null;
            if (commandSender instanceof Player) {
                String requiredPermission = "";
                if (getClass().isAnnotationPresent(CommandMeta.class)) {
                    CommandMeta annotation = getClass().getAnnotation(CommandMeta.class);
                    requiredPermission = annotation.permission();
                }
                if (!commandSender.hasPermission(requiredPermission)) {
                    throw new CommandException("You can not run this command as you do not carry the permission: " + requiredPermission);
                }
            }

            if(isSubCommandsOnly()) {
                if (args.length < 1) {
                    args = new String[1];
                    args[0] = "help";
                }
                if ((subCommand = getSubCommandFor(args[0])) == null) {
                    throw new CommandException("That Subcommand does not exist");
                }
            }
            if (subCommand == null && args.length > 0) {
                subCommand = getSubCommandFor(args[0]);
            }
            if (subCommand != null) {
                String[] choppedArgs = args.length < 2 ? new String[0] : Arrays.copyOfRange(args, 1, args.length);
                subCommand.onCommand(commandSender, command, s, choppedArgs);
                return true;
            }
            if (commandSender instanceof Player) {
                Optional<dev.tomr.parkutil.player.Player> player = Parkutil.getParkutil().getPlayerManager().getPlayer(((Player) commandSender).getUniqueId().toString());
                if (player.isPresent()) handleCommand(player.get(), args);
            } else if (commandSender instanceof BlockCommandSender) handleCommand((BlockCommandSender) commandSender, args);
            else if (commandSender instanceof ConsoleCommandSender) handleCommand((ConsoleCommandSender) commandSender, args);
            else handleCommandUnspecific(commandSender, args);

        } catch (CommandException e) {
            commandSender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    protected boolean isSubCommandsOnly() {
        return false;
    }

    public final Command getSubCommandFor(String s) {
        if (subCommands.containsKey(s)) return subCommands.get(s);
        for (String s1 : subCommands.keySet()) {
            if (s1.equalsIgnoreCase(s)) return subCommands.get(s1);
        }
        return null;
    }

    protected void handleCommand(dev.tomr.parkutil.player.Player player, String[] args) throws CommandException {
        throw new CommandException("No way to handle this command specified");
    }

    protected void handleCommand(ConsoleCommandSender commandSender, String[] args) throws CommandException {
        throw new CommandException("No way to handle this command specified");
    }

    protected void handleCommand(BlockCommandSender commandSender, String[] args) throws CommandException {
        throw new CommandException("No way to handle this command specified");
    }

    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {
        throw new CommandException("No way to handle this command specified");
    }

    protected String getFormattedName() {
        return superCommand == null ? name : superCommand.getFormattedName() + " " + name;
    }

    @Override
    public String toString() {
        return "Command > " + getFormattedName();
    }
}
