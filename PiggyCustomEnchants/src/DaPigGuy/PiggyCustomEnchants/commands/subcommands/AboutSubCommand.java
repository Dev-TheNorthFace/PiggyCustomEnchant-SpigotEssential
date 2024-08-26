package DaPigGuy.PiggyCustomEnchants.commands.subcommands.AboutSubCommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public class AboutSubCommand extends SubCommand {

    private final PiggyCustomEnchants plugin;

    public AboutSubCommand(PiggyCustomEnchants plugin) {
        super("about", "Displays basic information about the plugin");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String message = ChatColor.GREEN + "PiggyCustomEnchants version " + ChatColor.GOLD + plugin.getDescription().getVersion() + ChatColor.RESET + "\n" +
                ChatColor.GREEN + "PiggyCustomEnchants is a versatile custom enchantments plugin developed by DaPigGuy (MCPEPIG) and Aericio." + ChatColor.RESET + "\n" +
                "More information about our plugin can be found at " + ChatColor.GOLD + "https://piggydocs.aericio.net/" + ChatColor.GREEN + "." + ChatColor.RESET + "\n" +
                ChatColor.GRAY + "Copyright 2017 DaPigGuy; Licensed under the Apache License.";

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (plugin.areFormsEnabled()) {
                showForm(player, message);
                return;
            }
        }
        sender.sendMessage(message);
    }

    private void showForm(Player player, String message) {
        player.sendMessage(ChatColor.GREEN + "About PiggyCustomEnchants");
        player.sendMessage(message);
        player.sendMessage(ChatColor.GRAY + "Back");
    }

    @Override
    public String getPermission() {
        return "piggycustomenchants.command.ce.about";
    }
}