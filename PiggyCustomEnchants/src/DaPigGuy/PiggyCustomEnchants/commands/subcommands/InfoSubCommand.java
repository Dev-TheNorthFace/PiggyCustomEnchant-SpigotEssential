package DaPigGuy.PiggyCustomEnchants.commands.subcommands.InfoSubCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class InfoSubCommand extends Command {

    private final PiggyCustomEnchants plugin;

    public InfoSubCommand(PiggyCustomEnchants plugin) {
        super("info");
        this.plugin = plugin;
        setDescription("Get info on a custom enchantment");
        setUsage("/ce info <enchantment>");
        setPermission("piggycustomenchants.command.ce.info");
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player) && args.length < 1) {
            sender.sendMessage("Usage: /ce info <enchantment>");
            return false;
        }

        if (sender instanceof Player) {
            if (args.length == 1) {
                String enchantName = args[0];
                CustomEnchant enchant = CustomEnchantManager.getEnchantmentByName(enchantName);
                if (enchant == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid enchantment.");
                    return false;
                }
                showInfo((Player) sender, enchant);
                return true;
            } else {
                Player player = (Player) sender;
                showInputForm(player);
                return true;
            }
        } else {
            if (args.length < 1) {
                sender.sendMessage("Usage: /ce info <enchantment>");
                return false;
            }

            String enchantName = args[0];
            CustomEnchant enchant = CustomEnchantManager.getEnchantmentByName(enchantName);
            if (enchant == null) {
                sender.sendMessage(ChatColor.RED + "Invalid enchantment.");
                return false;
            }

            sender.sendMessage(ChatColor.GREEN + enchant.getDisplayName() + "\n" +
                    "ID: " + enchant.getId() + "\n" +
                    "Description: " + enchant.getDescription() + "\n" +
                    "Type: " + Utils.TYPE_NAMES[enchant.getItemType()] + "\n" +
                    "Rarity: " + Utils.RARITY_NAMES[enchant.getRarity()] + "\n" +
                    "Max Level: " + enchant.getMaxLevel());
            return true;
        }
    }

    private void showInputForm(Player player) {

        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 9, "Enter Enchantment Name");
        ItemStack item = new ItemStack(org.bukkit.Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "Enter enchantment name");
            item.setItemMeta(meta);
        }

        inv.setItem(4, item);
        player.openInventory(inv);
    }

    private void showInfo(Player player, CustomEnchant enchant) {

        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GREEN + enchant.getDisplayName() + " Enchantment");
        ItemStack info = new ItemStack(org.bukkit.Material.BOOK);
        ItemMeta meta = info.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + enchant.getDisplayName());
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.RESET + "ID: " + enchant.getId());
            lore.add(ChatColor.RESET + "Description: " + enchant.getDescription());
            lore.add(ChatColor.RESET + "Type: " + Utils.TYPE_NAMES[enchant.getItemType()]);
            lore.add(ChatColor.RESET + "Rarity: " + Utils.RARITY_NAMES[enchant.getRarity()]);
            lore.add(ChatColor.RESET + "Max Level: " + enchant.getMaxLevel());
            meta.setLore(lore);
            info.setItemMeta(meta);
        }

        inv.setItem(4, info);
        ItemStack backButton = new ItemStack(org.bukkit.Material.BARRIER);
        ItemMeta backMeta = backButton.getItemMeta();
        if (backMeta != null) {
            backMeta.setDisplayName(ChatColor.RED + "Back");
            backButton.setItemMeta(backMeta);
        }
        inv.setItem(8, backButton);

        player.openInventory(inv);
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
        }
        return completions;
    }
}