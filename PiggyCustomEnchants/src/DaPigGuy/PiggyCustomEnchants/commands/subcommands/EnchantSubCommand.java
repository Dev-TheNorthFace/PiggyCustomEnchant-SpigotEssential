package DaPigGuy.PiggyCustomEnchants.commands.subcommands.EnchantSubCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import java.util.ArrayList;
import java.util.List;

public class EnchantSubCommand extends Command {

    private final PiggyCustomEnchants plugin;

    public EnchantSubCommand(PiggyCustomEnchants plugin) {
        super("enchant");
        this.plugin = plugin;
        setDescription("Apply an enchantment to an item");
        setUsage("/ce enchant <enchantment> <level> [player]");
        setPermission("piggycustomenchants.command.ce.enchant");
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player) && args.length < 2) {
            sender.sendMessage("Usage: /ce enchant <enchantment> <level> [player]");
            return false;
        }

        String enchantName = args[0];
        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Enchantment level must be an integer");
            return false;
        }

        Player target = args.length > 2 ? Bukkit.getPlayer(args[2]) : (Player) sender;
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Invalid player.");
            return false;
        }

        CustomEnchant enchant = CustomEnchantManager.getEnchantmentByName(enchantName);
        if (enchant == null) {
            sender.sendMessage(ChatColor.RED + "Invalid enchantment.");
            return false;
        }

        ItemStack item = target.getInventory().getItemInMainHand();
        if (!sender.hasPermission("piggycustomenchants.overridecheck")) {
            if (!Utils.itemMatchesItemType(item, enchant.getItemType())) {
                sender.sendMessage(ChatColor.RED + "The item is not compatible with this enchant.");
                return false;
            }
            if (level > enchant.getMaxLevel()) {
                sender.sendMessage(ChatColor.RED + "The max level is " + enchant.getMaxLevel() + ".");
                return false;
            }
            if (item.getAmount() > 1) {
                sender.sendMessage(ChatColor.RED + "You can only enchant one item at a time.");
                return false;
            }
            if (!Utils.checkEnchantIncompatibilities(item, enchant)) {
                sender.sendMessage(ChatColor.RED + "This enchant is not compatible with another enchant.");
                return false;
            }
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "Enchanted Item");
            item.addUnsafeEnchantment(enchant.getEnchantment(), level);
            target.getInventory().setItemInMainHand(item);
            sender.sendMessage(ChatColor.GREEN + "Item successfully enchanted.");
        } else {
            sender.sendMessage(ChatColor.RED + "Failed to apply enchantment.");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (String enchantment : CustomEnchantManager.getEnchantmentNames()) {
                if (enchantment.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(enchantment);
                }
            }
        } else if (args.length == 2) {
            completions.add("1");
            completions.add("2");
        } else if (args.length == 3) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                    completions.add(player.getName());
                }
            }
        }
        return completions;
    }
}