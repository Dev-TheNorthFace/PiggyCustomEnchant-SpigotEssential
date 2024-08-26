package DaPigGuy.PiggyCustomEnchants.commands.CustomEnchantsCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomEnchantsCommand extends Command {

    private final PiggyCustomEnchants plugin;
    private final List<SubCommand> subCommands = new ArrayList<>();

    public CustomEnchantsCommand(PiggyCustomEnchants plugin) {
        super("ce");
        this.plugin = plugin;
        this.setDescription("Main command for PiggyCustomEnchants");
        this.setUsage("/ce <subcommand>");
        initializeSubCommands();
    }

    private void initializeSubCommands() {
        subCommands.add(new AboutSubCommand(plugin));
        subCommands.add(new EnchantSubCommand(plugin));
        subCommands.add(new InfoSubCommand(plugin));
        subCommands.add(new ListSubCommand(plugin));
        subCommands.add(new NBTSubCommand(plugin));
        subCommands.add(new RemoveSubCommand(plugin));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (plugin.areFormsEnabled()) {
                    showMenu(player);
                    return true;
                }
            }
            sender.sendMessage("Usage: /ce <" + String.join("|", getSubCommandNames()) + ">");
            return true;
        }

        String subCommandName = args[0];
        SubCommand subCommand = getSubCommand(subCommandName);
        if (subCommand != null) {
            subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
            return true;
        }

        sender.sendMessage("Unknown subcommand. Usage: /ce <" + String.join("|", getSubCommandNames()) + ">");
        return true;
    }

    private SubCommand getSubCommand(String name) {
        for (SubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(name)) {
                return subCommand;
            }
        }
        return null;
    }

    private List<String> getSubCommandNames() {
        List<String> names = new ArrayList<>();
        for (SubCommand subCommand : subCommands) {
            names.add(subCommand.getName());
        }
        return names;
    }
    
    public static class CustomEnchantsTabCompleter implements TabCompleter {

        private final CustomEnchantsCommand command;

        public CustomEnchantsTabCompleter(CustomEnchantsCommand command) {
            this.command = command;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            if (args.length == 1) {
                return command.getSubCommandNames();
            }
            return new ArrayList<>();
        }
    }
}