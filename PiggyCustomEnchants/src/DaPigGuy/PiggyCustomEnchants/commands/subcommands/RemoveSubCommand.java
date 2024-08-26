package DaPigGuy.PiggyCustomEnchants.commands.subcommands.RemoveSubCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoveSubCommand extends Command {

    private final PiggyCustomEnchants plugin;

    public RemoveSubCommand(PiggyCustomEnchants plugin) {
        super("remove");
        this.plugin = plugin;
        setDescription("Remove a custom enchantment from an item");
        setUsage("/ce remove <enchantment> <player>");
        setPermission("piggycustomenchants.command.ce.remove");
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                openRemoveForm((Player) sender);
                return true;
            }
            if (args.length >= 2) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid player.");
                    return false;
                }

                CustomEnchant enchant = CustomEnchantManager.getEnchantmentByName(args[0]);
                if (enchant == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid enchantment.");
                    return false;
                }

                ItemStack item = target.getInventory().getItemInMainHand();
                if (!item.hasItemMeta()) {
                    sender.sendMessage(ChatColor.RED + "Item does not have the specified enchantment.");
                    return false;
                }

                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasEnchant(enchant.getEnchantment())) {
                    item.removeEnchantment(enchant.getEnchantment());
                    target.getInventory().setItemInMainHand(item);
                    sender.sendMessage(ChatColor.GREEN + "Enchantment successfully removed.");
                    target.sendMessage(ChatColor.GREEN + "Your item has had the enchantment removed.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Item does not have the specified enchantment.");
                }

                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /ce remove <enchantment> <player>");
                return false;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be used in-game.");
            return false;
        }
    }
}